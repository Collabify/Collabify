package space.collabify.collabify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.LoginActivity;
import com.spotify.sdk.android.playback.Config;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;

import kaaes.spotify.webapi.android.SpotifyService;
import space.collabify.collabify.CollabifyClient;
import space.collabify.collabify.R;

import space.collabify.collabify.controls.ImageToggleButton;
import space.collabify.collabify.managers.AppManager;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.requests.PlaylistRequest;

/**
 * This file was born on March 11 at 14:11
 */
public class BasePlayerFragment extends Fragment implements ConnectionStateCallback, PlayerNotificationCallback {
    private static final String TAG = BasePlayerFragment.class.getSimpleName();

    private Player mPlayer;
    private Playlist mPlaylist;
    private SpotifyService mAPI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_player, container, false);

        if (AppManager.getInstance().getUser().getRole().isDJ()) {
            setUpForDJ(rootView);
        }
        else {
            setUpForCollabifier(rootView);
        }

        return rootView;
    }

    private void setUpForCollabifier(View rootView) {
        ImageToggleButton playButton = (ImageToggleButton) rootView.findViewById(R.id.player_play_pause);
        playButton.disable();
    }

    private void setUpForDJ(View rootView) {
        String clientID = getResources().getString(R.string.client_id);
        Config mPlayerConfig = new Config(getActivity().getApplicationContext(),
                                            AppManager.getInstance().getUser().getAccessToken(),
                                            clientID);

        mPlayer = Spotify.getPlayer(mPlayerConfig, this, new Player.InitializationObserver() {

            @Override
            public void onInitialized(Player player) {
                mPlayer.addConnectionStateCallback(BasePlayerFragment.this);
                mPlayer.addPlayerNotificationCallback(BasePlayerFragment.this);
                mAPI = AppManager.getInstance().getSpotifyApi().getApi();
                mPlaylist = CollabifyClient.getInstance().getEventPlaylist(new PlaylistRequest());
                bindButtons();
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), R.string.message_player_init_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void bindButtons() {
        Toast.makeText(getActivity().getApplicationContext(), "Got to Bind buttons", Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {
        Toast.makeText(getActivity().getApplicationContext(), R.string.message_player_playback_error, Toast.LENGTH_LONG).show();
    }


}