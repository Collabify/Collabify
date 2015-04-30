package space.collabify.android.fragments;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import space.collabify.android.PlayerHandler;
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
public class BasePlayerFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = BasePlayerFragment.class.getSimpleName();

    private AppManager mAppManager;

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
    private boolean mViewRestored = false;

    private PlayerFragmentListener mListener;

    public interface PlayerFragmentListener{
        public PlayerHandler getPlayerHandler();
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        isDJ = AppManager.getInstance().getUser().getRole().isDJ();
        mAppManager = AppManager.getInstance();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PlayerFragmentListener) activity;
        }catch(ClassCastException ex){
            throw new ClassCastException(activity.toString() + " must implement PlayerFragmentListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_player, container, false);

        mViewRestored = false;

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

        mListener.getPlayerHandler().updateSong();

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
                        mListener.getPlayerHandler().updateSong();
                    }

                    @Override
                    public void success(Response response) {
                        mListener.getPlayerHandler().updateSong();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updatePlayerView();
                            }
                        });
                        
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mListener.getPlayerHandler().updateSong();
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

    public void updatePlayerView() {
        // Song on queue
        Song currentSong = mListener.getPlayerHandler().getCurrentSong();
        if (currentSong != null) {
            mSongTitle.setText(currentSong.getTitle());
            mSongArtist.setText(currentSong.getAlbum() + "\n" + currentSong.getArtist());
            mPlayPauseBtn.enable();
            mNextSongBtn.setImageResource(R.drawable.ic_fast_forward_white_48dp);
            if(currentSong.getArtwork() != null && !currentSong.getArtwork().isEmpty()) {
                Picasso.with(getActivity()).load(currentSong.getArtwork()).into(mAlbumImage);
            }
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
        //updateSong();
        updatePlayerView();
    }


    @Override
    public void onPause() {
      super.onPause();
      mMicrophoneBtn.setChecked(false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        mViewRestored = true;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(mViewRestored){
            Player player = mListener.getPlayerHandler().getPlayer();
            if (isChecked) {
                // play song
                if (mListener.getPlayerHandler().getCurrSongDidStart()) {
                    player.resume();
                }
                else {
                    mListener.getPlayerHandler().playCurrentSong();
                }
            }
            else {
                //pause song
                player.pause();
            }
        }
    }
}