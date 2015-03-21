package space.collabify.collabify;

import android.location.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import space.collabify.collabify.models.Event;

/**
 * This file was born on March 11 at 13:43
 */
public class ServerManager {
    private static ServerManager mInstance;

    private ServerManager(){
        //private because singleton
        //TODO probably set up some server connections
    }

    /**
     * Gets instance of ServerManager singleton
     * @return instance of the server manager
     */
    public static ServerManager getInstance(){
        if(mInstance == null){
            //create new instance
            mInstance = new ServerManager();
        }

        return mInstance;
    }

    public List<Event> getEvents(LoadEventsRequest request){
        //TODO fill in with actual server stuff

        //fake some server delay
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Android", false, null));
        events.add(new Event("iPhone", false, null));
        events.add(new Event("WindowsMobile", false, null));
        events.add(new Event("Blackberry", false, null));
        events.add(new Event("WebOS", false, null));
        events.add(new Event("Ubuntu", false, null));
        events.add(new Event("Windows7", false, null));
        events.add(new Event( "Max OS X", false, null));
        events.add(new Event("Linux", false, null));
        events.add(new Event("OS/2", false, null));
        events.add(new Event("More", false, null));
        events.add(new Event("List", false, null));
        events.add(new Event("Items", false, null));
        events.add(new Event("Here", false, null));


        return events;
    }
}
