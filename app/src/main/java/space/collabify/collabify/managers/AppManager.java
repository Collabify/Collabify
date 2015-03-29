package space.collabify.collabify.managers;

import android.content.Context;

import com.spotify.sdk.android.authentication.AuthenticationClient;

import space.collabify.collabify.models.User;

/**
 * This file was born on March 11 at 13:43
 */
public class AppManager {
    private static AppManager instance;

    private User user;

    private AppManager(){
      user = new User();
        //private because singleton
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

    /** Logs the to user out of spotify */
    public void spotifyLogout(Context context){
        AuthenticationClient.logout(context);
    }

    /** Clears any user, event or other data that shouldn't persist after a
     * user hits 'logout' and is returned to the login screen
     */
    public void clearData() {
        user = new User();
    }
}
