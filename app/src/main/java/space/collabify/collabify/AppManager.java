package space.collabify.collabify;

import space.collabify.collabify.models.User;

/**
 * This file was born on March 11 at 13:43
 */
public class AppManager {
    private static AppManager mInstance;

    private User user;

    private AppManager(){
        //private because singleton
    }

    /**
     * Gets instance of AppManager singleton
     * @return instance of the app manager
     */
    public static AppManager getInstance(){
        if(mInstance == null){
            //create new instance
            mInstance = new AppManager();
        }

        return mInstance;
    }

    public User getUser() {
        return user;
    }
}
