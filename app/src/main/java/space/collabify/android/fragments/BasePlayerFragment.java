package space.collabify.android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.util.List;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;

import space.collabify.android.controls.ImageToggleButton;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Playlist;
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
    private ImageToggleButton mPlayPauseBtn;

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
        updateSong();
        if (isDJ) {
            setUpForDJ(rootView);
        }
        else {
            setUpForCollabifier(rootView);
        }

        return rootView;
    }

    private void setUpForCollabifier(View rootView) {
        mPlayPauseBtn = (ImageToggleButton) rootView.findViewById(R.id.player_play_pause);
        mPlayPauseBtn.disable();
    }

    private void setUpForDJ(View rootView) {
        mPlayPauseBtn = (ImageToggleButton) rootView.findViewById(R.id.player_play_pause);
        mPlayPauseBtn.setOnCheckedChangeListener(this);
    }

    private void setUpPlayer() {
        mAppManager.loadEventPlaylist(new CollabifyCallback<List<Song>>() {
            @Override
            public void exception(Exception e) {

            }

            @Override
            public void success(List<Song> songs, Response response) {

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
        if (mSongTitle != null) {
            mCurrentSong = mAppManager.getCurrentSong();
            if (mCurrentSong != null) {
                mSongTitle.setText(mCurrentSong.getTitle());
            }
            else {
                mSongTitle.setText(getText(R.string.label_nothing_to_play));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateSong();
    }

    @Override
    public void onLoggedIn() {
        Toast.makeText(getActivity().getApplicationContext(), "Logged In cb from Player", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoggedOut() {
        Toast.makeText(getActivity().getApplicationContext(), "Logged out cb from Player", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoginFailed(Throwable throwable) {
        Toast.makeText(getActivity().getApplicationContext(), "Login failed cb from Player", Toast.LENGTH_LONG).show();

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
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            // play song
            if (currSongDidStart) {
                mPlayer.resume();
            }
            else {
                if(mPlayer != null && mCurrentSong != null) {
                    mPlayer.play("spotify:track:" + mCurrentSong.getId());
                    currSongDidStart = true;
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