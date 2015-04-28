package space.collabify.android;

import android.app.Activity;
import android.test.ApplicationTestCase;
import android.util.Log;
import android.widget.Toast;

import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import kaaes.spotify.webapi.android.SpotifyApi;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;

/**
 * This file was born on April 28, at 10:47
 */
public class PlayerHandler implements PlayerNotificationCallback, ConnectionStateCallback {
    private static final String TAG = PlayerHandler.class.getSimpleName();

    private Player mPlayer;
    private Activity mCallerActivity;

    private boolean currSongDidStart = false;

    public PlayerHandler(Activity callingActivity){
        this.mCallerActivity = callingActivity;
        if(AppManager.getInstance().getUser().getRole().isDJ()){
            setUpPlayer();
        }
    }

    public Player getPlayer(){
        return mPlayer;
    }

    public boolean getCurrSongDidStart(){
        return currSongDidStart;
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        if (eventType.equals(EventType.PLAY)) {
            currSongDidStart = true;
        }
        if (eventType.equals(EventType.TRACK_END)) {
            currSongDidStart = false;
            /*
            if (mPlayPauseBtn != null && mPlayPauseBtn.isChecked()) {
                mPlayPauseBtn.setChecked(false);
            }
            */
        }
    }

    private void setUpPlayer() {
        AppManager.getInstance().loadEventPlaylist(new CollabifyCallback<Playlist>() {
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

        String clientID = mCallerActivity.getResources().getString(R.string.client_id);
        Config mPlayerConfig = new Config(mCallerActivity.getApplicationContext(),
                AppManager.getInstance().getUser().getAccessToken(),
                clientID);
        mPlayer = Spotify.getPlayer(mPlayerConfig, this, new Player.InitializationObserver() {

            @Override
            public void onInitialized(Player player) {
                mPlayer.addConnectionStateCallback(PlayerHandler.this);
                mPlayer.addPlayerNotificationCallback(PlayerHandler.this);
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(mCallerActivity.getApplicationContext(), R.string.message_player_init_error, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {
        Toast.makeText(mCallerActivity.getApplicationContext(), R.string.message_player_playback_error, Toast.LENGTH_LONG).show();
    }

    public void onDestroy(){
        Spotify.destroyPlayer(this);
    }

    @Override
    public void onLoggedIn() {
//        Toast.makeText(mCallerActivity.getApplicationContext(), "Logged In cb from Player", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLoggedOut() {
//        Toast.makeText(mCallerActivity.getApplicationContext(), "Logged out cb from Player", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onLoginFailed(Throwable throwable) {
//        Toast.makeText(mCallerActivity.getApplicationContext(), "Login failed cb from Player", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onTemporaryError() {
        Toast.makeText(mCallerActivity.getApplicationContext(), R.string.message_player_temporary_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d(TAG, "Connection message from Player: " + s);
    }
}
