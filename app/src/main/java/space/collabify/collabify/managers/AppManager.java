package space.collabify.collabify.managers;

import android.content.Context;

import com.spotify.sdk.android.authentication.AuthenticationClient;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyApi;
import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.User;
import space.collabify.collabify.CollabifyClient;

/**
 * This file was born on March 11 at 13:43
 */
public class AppManager {
    private static AppManager instance;
    private static CollabifyClient mClient;
    private static SpotifyApi mSpotifyApi;

    private User user;
    private Event event;

    private AppManager(){
      //private because singleton
      newUser();
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
            mClient = CollabifyClient.getInstance();
        }

        return instance;
    }

    public User getUser() {
      if (user == null) {
        newUser();
      }
      return user;
    }

    public Event getEvent() {
      return event;
    }

    public void setSpotifyApi(SpotifyApi spotifyApi) {
        this.mSpotifyApi = spotifyApi;
    }

    public SpotifyApi getSpotifyApi() {
        return mSpotifyApi;
    }

    /** Logs the to user out of spotify */
    public void spotifyLogout(Context context){
        AuthenticationClient.logout(context);
    }

    /** Clears any user, event or other data that shouldn't persist after a
     * user hits 'logout' and is returned to the login screen
     */
    public void clearData() {
      user = null;
      event = null;
      mClient.resetPlaylist();
    }

    private void newUser() {
      user = new User("NEW USER", "12345");
      user.setRole("NoRole");
    }

  /**
   * Create a local copy of the event
   * @param e Event to create
   */
    public void createEvent(Event e) {
      // Add DJ to Event
      ArrayList<User> userlist = new ArrayList<>();
      userlist.add(getUser());
      e.setmUserList(userlist);

      event = e;
      mClient.createEvent(event, user);
    }

  /**
   * Join event and set local data
   * @param e Event to join
   */
    public void joinEvent(Event e) {
      event = e;
      event.getmUserList().add(getUser());
      mClient.joinEvent(event, user);
    }
}
