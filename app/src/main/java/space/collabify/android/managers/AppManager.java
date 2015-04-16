package space.collabify.android.managers;

import android.content.Context;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.squareup.okhttp.Call;

import retrofit.Callback;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import retrofit.ResponseCallback;
import space.collabify.android.collabify.api.CollabifyApi;
import space.collabify.android.collabify.api.CollabifyApiException;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.models.Event;
import space.collabify.android.models.Role;
import space.collabify.android.models.User;

/**
 * This file was born on March 11 at 13:43
 */
public class AppManager {
    private static AppManager instance;
    private static SpotifyApi mSpotifyApi;
    private static CollabifyApi mCollabifyApi;

    private User user;
    private Event event;
    private space.collabify.android.models.Playlist mEventPlaylist;

    private boolean eventUpdating = false;
    private boolean usersUpdating = false;
    private boolean playlistUpdating = false;

    private AppManager(){
        mCollabifyApi = new CollabifyApi();
    }

    /**
     * Gets instance of AppManager singleton
     *
     * @return instance of the app manager
     */
    public static AppManager getInstance(){
        if(instance == null){
            //create new instance
            instance = new AppManager();
        }

        return instance;
    }

    /**
     * Get the current logged in User
     *
     * @return
     */
    public User getUser() {
        if (user == null) {
            newUser();
        }
        return user;
    }

    /**
     * Get the event the user is currently attending
     *
     * @return
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Set the SpotifyApi object
     *
     * @param spotifyApi
     */
    public void setSpotifyApi(SpotifyApi spotifyApi) {
        this.mSpotifyApi = spotifyApi;
    }

    /**
     * Get the SpotifyApi
     *
     * @return
     */
    public SpotifyApi getSpotifyApi() {
        return mSpotifyApi;
    }

    /**
     * Logs the user out of spotify
     * @param context
     */
    public void spotifyLogout(Context context){
        AuthenticationClient.logout(context);
    }

    /**
     * Clears any user, event or other data that shouldn't persist after a
     * user hits 'logout' and is returned to the login screen
     *
     */
    public void clearData() {
        user = null;
        event = null;
        mCollabifyApi.clearCurrentUserId();
    }

    /**
     * Create a local copy of the event
     *
     * @param e Event to create
     */
    public void createEvent(Event e, Callback c) {
        // Add DJ to Event
        event = e;
    }

    /**
     * Join event and set local data
     *
     * @param e Event to join
     */
    public void joinEvent(Event e, Callback c) {
        event = e;
    }

    /**
     * Leave an event as a non-DJ
     */
    public void leaveEvent(ResponseCallback c) {
        try {
            mCollabifyApi.leaveEvent(event.getId().toString(), c);
        } catch (CollabifyApiException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void newUser() {
        user = new User("99999", "99999");
        user.setRole(Role.NO_ROLE);
    }
}
