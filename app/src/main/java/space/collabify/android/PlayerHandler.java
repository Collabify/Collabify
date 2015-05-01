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

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Song;

/**
 * This file was born on April 28, at 10:47
 */
public class PlayerHandler implements PlayerNotificationCallback, ConnectionStateCallback, AppManager.OnSongAddListener {
    private static final String TAG = PlayerHandler.class.getSimpleName();

    private Song mCurrentSong;

    private Player mPlayer;
    private Activity mCallerActivity;
    private PlayerHandlerListener mListener;

    private boolean currSongDidStart = false;
    private boolean mHasCurrentSongChanged = false;
    private boolean mSkippingSong = false;

    //shouldn't need this, but sometimes queuing same song multiple times in a row
    private String mLastQueuedSongId;

    public PlayerHandler(Activity callingActivity, PlayerHandlerListener listener){
        this.mListener = listener;
        this.mCallerActivity = callingActivity;
        if(AppManager.getInstance().getUser().getRole().isDJ()){
            setUpPlayer();
        }
    }

    public Song getCurrentSong() {
        if(mCurrentSong == null){
            updateSong();
        }

        return mCurrentSong;
    }


    public interface PlayerHandlerListener{
        void startNextSong();
    }

    public void attachListener(PlayerHandlerListener listener){
        mListener = listener;
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
        if(eventType.equals(EventType.SKIP_NEXT)){
            mSkippingSong = true;
        }

        if (eventType.equals(EventType.TRACK_END)) {
            currSongDidStart = false;
            updateSong();
            queueNextSong();
            mListener.startNextSong();
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

        updateSong();

        //if the playlist already has songs in it, should re queue existing
        queueCurrentSong();
        queueNextSong();

        //register to get called when songs are added, so that if there are
        //no songs on the list, the 1st/2nd song can get queued so they don't get skipped over
        AppManager.getInstance().registerSongAddListener(this);
    }

    /**
     * Fetches the current song from the server, and updates mCurrentsong
     */
    public void updateSong() {
        AppManager.getInstance().getCurrentSong(new CollabifyCallback<Song>() {
            @Override
            public void exception(Exception e) {
                Log.w(TAG, "Couldn't getCurrentSong: " + e.toString());
            }

            @Override
            public void success(Song song, Response response) {
                mHasCurrentSongChanged = false;
                if (mCurrentSong == null || song == null || !mCurrentSong.getId().equals(song.getId())) {
                    mHasCurrentSongChanged = true;
                    mCurrentSong = song;
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.w(TAG, "Failed to get current song: " + error.toString());
            }
        });
    }


    /**
     * Plays the current song in the playlist via spotify player.
     */
    public void playCurrentSong(){
        if(mCurrentSong != null) {
            mPlayer.play("spotify:track:" + mCurrentSong.getId());
        }else {
            Log.w(TAG, "Current song was null, couldn't play");
        }
    }

    /**
     * Stops the current song playback, gets the next and starts playback.
     * Call after the 'next' button is hit
     */
    public void nextSong(){
        currSongDidStart = false;
        mPlayer.skipToNext();
    }

    public void queueCurrentSong() {
        queueSong(mCurrentSong);
    }


    public void queueNextSong(){
        Song nextSong = AppManager.getInstance().getOnDeckSong();
        queueSong(nextSong);
    }

    private void queueSong(Song song){
        if(song == null || song.getId() == null){
            Log.w(TAG, "song or id is null, can't queue it");
            return;
        }
        if(mLastQueuedSongId != null && mLastQueuedSongId.equals(song.getId())){
            Log.w(TAG, "can't queue same song twice in a row");
            return;
        }
        if(!mPlayer.isShutdown() && mPlayer.isInitialized()){
            mPlayer.queue("spotify:track:" + song.getId());
            mLastQueuedSongId = song.getId();
        }else{
            Log.w(TAG, "Player is shutdown or not initialized yet...");
        }
    }


    @Override
    public void onSongAdded(Song song) {
        //if there is only a currently playing song (next song is null), then we should queue this song?
        List<Song> nextSongs = AppManager.getInstance().getCurrentSongList();

        if(nextSongs == null || nextSongs.size() <= 2) {
            queueSong(song);
        }
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
