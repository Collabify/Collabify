package space.collabify.collabify.managers;

import android.content.Context;

import com.spotify.sdk.android.authentication.AuthenticationClient;

import java.util.ArrayList;

import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.User;

/**
 * This file was born on March 11 at 13:43
 */
public class AppManager {
    private static AppManager instance;

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
        }

        return instance;
    }

    public User getUser() {
        return user;
    }

    public Event getEvent() {
      return event;
    }

    /** Logs the to user out of spotify */
    public void spotifyLogout(Context context){
        AuthenticationClient.logout(context);
    }

    /** Clears any user, event or other data that shouldn't persist after a
     * user hits 'logout' and is returned to the login screen
     */
    public void clearData() {
      newUser();
      event = null;
    }

    private void newUser() {
      user = new User("NEW USER", 12345);
    }

    public void createEvent(Event e) {
      // Add DJ to Event
      ArrayList<User> userlist = new ArrayList<>();
      userlist.add(user);
      e.setmUserList(userlist);

      event = e;
    }
}
