package space.collabify.android.fragments;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;

import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.controls.ImageToggleButton;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.managers.CollabifyResponseCallback;
import space.collabify.android.models.Song;

/**
 * This file was born on March 11 at 14:11
 */
public class BasePlayerFragment extends Fragment implements ConnectionStateCallback, PlayerNotificationCallback, CompoundButton.OnCheckedChangeListener {
    private static final String TAG = BasePlayerFragment.class.getSimpleName();

    private AppManager mAppManager;
    private Player mPlayer;
    private Song mCurrentSong;

    private TextView mSongTitle;
    private TextView mSongArtist;
    private ImageView mAlbumImage;
    private ImageToggleButton mPlayPauseBtn;
    private ImageButton mNextSongBtn;

    private ImageToggleButton mMicrophoneBtn;
    private boolean isRecording;
    private AudioRecord arec;
    private AudioTrack atrack;
    private Thread rThread;

    private boolean isDJ;
    private boolean currSongDidStart = false;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        isDJ = AppManager.getInstance().getUser().getRole().isDJ();
        mAppManager = AppManager.getInstance();
        if (isDJ) {
            setUpPlayer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_player, container, false);

        mSongTitle = (TextView) rootView.findViewById(R.id.player_song_title);
        mSongArtist = (TextView) rootView.findViewById(R.id.player_song_artist);
        mAlbumImage = (ImageView) rootView.findViewById(R.id.player_song_album_art);
        mNextSongBtn = (ImageButton) rootView.findViewById(R.id.player_skip_next);

        if (isDJ) {
            setUpForDJ(rootView);
        }
        else {
            setUpForCollabifier(rootView);
        }

        updateSong();

        return rootView;
    }

    private void setUpForCollabifier(View rootView) {
        mPlayPauseBtn = (ImageToggleButton) rootView.findViewById(R.id.player_play_pause);
        mPlayPauseBtn.disable();
    }

    private void setUpForDJ(View rootView) {
        mPlayPauseBtn = (ImageToggleButton) rootView.findViewById(R.id.player_play_pause);
        mPlayPauseBtn.setOnCheckedChangeListener(this);

        mNextSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAppManager.nextSong(new CollabifyResponseCallback() {
                    @Override
                    public void exception(Exception e) {
                        updateSong();
                    }

                    @Override
                    public void success(Response response) {
                        updateSong();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        updateSong();
                    }
                });

            }
        });
        mMicrophoneBtn = (ImageToggleButton) rootView.findViewById(R.id.player_microphone);
        mMicrophoneBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
              // start playing audio
              isRecording = true;

              rThread = new Thread(new Runnable() {
                public void run() {
                  android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
                  int bufferSize = AudioRecord.getMinBufferSize(11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT);
                  arec = new AudioRecord(MediaRecorder.AudioSource.MIC, 11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
                  atrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM);
                  atrack.setPlaybackRate(11025);
                  byte[] buffer = new byte[bufferSize];
                  arec.startRecording();
                  atrack.play();
                  Log.d("RECORDING", "Hay is for horses!");
                  while (isRecording) {
                    arec.read(buffer, 0, bufferSize);
                    atrack.write(buffer, 0, buffer.length);
                  }
                  arec.stop();
                  arec.release();
                }
              });
              rThread.start();
            } else {
              // stop playing audio
              isRecording = false;
              try {
                rThread.join();
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
          }
        });
    }

    private void setUpPlayer() {
        mAppManager.loadEventPlaylist(new CollabifyCallback<Playlist>() {
            @Override
            public void exception(Exception e) {

            }

            @Override
            public void success(Playlist playlist, Response response) {

            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

        String clientID = getResources().getString(R.string.client_id);
        Config mPlayerConfig = new Config(getActivity().getApplicationContext(),
                AppManager.getInstance().getUser().getAccessToken(),
                clientID);
        mPlayer = Spotify.getPlayer(mPlayerConfig, this, new Player.InitializationObserver() {

            @Override
            public void onInitialized(Player player) {
                mPlayer.addConnectionStateCallback(BasePlayerFragment.this);
                mPlayer.addPlayerNotificationCallback(BasePlayerFragment.this);
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.message_player_init_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateSong() {

        if (currSongDidStart) {
            return;
        }

        if (mSongTitle != null) {
            mAppManager.getCurrentSong(new CollabifyCallback<Song>() {
                @Override
                public void exception(Exception e) {

                }

                @Override
                public void success(Song song, Response response) {

                    if (mCurrentSong == null || song == null || !mCurrentSong.getId().equals(song.getId())) {
                        mCurrentSong = song;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updatePlayerView();
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });

        }
    }

    private void updatePlayerView() {
        // Song on queue
        if (mCurrentSong != null) {
            mSongTitle.setText(mCurrentSong.getTitle());
            mSongArtist.setText(mCurrentSong.getAlbum() + "\n" + mCurrentSong.getArtist());
            mPlayPauseBtn.enable();
            mNextSongBtn.setImageResource(R.drawable.ic_fast_forward_white_48dp);
            Picasso.with(getActivity()).load(mCurrentSong.getArtwork()).into(mAlbumImage);
        }
        // Nothing to play
        else {
            mSongTitle.setText(getText(R.string.label_nothing_to_play));
            mSongArtist.setText("");
            mAlbumImage.setImageResource(R.drawable.ic_album);
            mPlayPauseBtn.disable();
            mNextSongBtn.setImageResource(R.drawable.ic_fast_forward_grey600_48dp);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSong();
    }

    @Override
    public void onLoggedIn() {
//        Toast.makeText(getActivity().getApplicationContext(), "Logged In cb from Player", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoggedOut() {
//        Toast.makeText(getActivity().getApplicationContext(), "Logged out cb from Player", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginFailed(Throwable throwable) {
//        Toast.makeText(getActivity().getApplicationContext(), "Login failed cb from Player", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTemporaryError() {
        Toast.makeText(getActivity().getApplicationContext(), R.string.message_player_temporary_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d(TAG, "Connection message from Player: " + s);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        if (eventType.equals(EventType.PLAY)) {
            currSongDidStart = true;
        }
        if (eventType.equals(EventType.TRACK_END)) {
            if (mPlayPauseBtn != null && mPlayPauseBtn.isChecked()) {
                mPlayPauseBtn.setChecked(false);
                currSongDidStart = false;
            }
        }
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {
        Toast.makeText(getActivity().getApplicationContext(), R.string.message_player_playback_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    @Override
    public void onPause() {
      super.onPause();
      isRecording = false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // play song
            if (currSongDidStart) {
                mPlayer.resume();
            }
            else {
                if(mPlayer != null && mCurrentSong != null) {
                    mPlayer.play("spotify:track:" + mCurrentSong.getId());
                }else {
                    Log.w(TAG, "Either player or currentSong was NULL, couldn't start playback");
                }
            }
        }
        else {
            //pause song
            mPlayer.pause();
        }
    }
}