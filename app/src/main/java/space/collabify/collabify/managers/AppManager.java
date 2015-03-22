package space.collabify.collabify.managers;

import space.collabify.collabify.models.User;

/**
 * This file was born on March 11 at 13:43
 */
public class AppManager {
    private static AppManager instance;

    private User user;

    private AppManager(){
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
}
