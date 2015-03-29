package space.collabify.collabify;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;

/**
 * Created by ricardolopez on 3/22/15.
 */
public class CollabifyClient {

    private static CollabifyClient instance;

    private Event mJoinedEvent;
    private Playlist mEventPlaylist;

    private CollabifyClient() {

    }

    public static CollabifyClient getInstance() {
        if (instance == null) {
            instance = new CollabifyClient();
        }
        return instance;
    }

    /**
     * Queries the server for events
     * @param request query information
     * @return list of events based off of request param
     */
    public List<Event> getEvents(LoadEventsRequest request){
        //TODO fill in with actual server stuff

        //fake some server delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Event> events = new ArrayList<>();
        events.add(new Event("Android", false, null));
        events.add(new Event("iPhone", true, "$$$"));
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

    public void joinEvent(Event event, User user){
        //TODO: implementation
    }


    public Playlist getEventPlaylist(){
        //TODO: actual server stuff to get the playlist
        ArrayList<Song> fakeSongList = new ArrayList<>();
        fakeSongList.add(new Song("on the sunny side of the street", "sonny stitt, etc.", "sonny side up", 1957, "0", "", false));
        fakeSongList.add(new Song("the eternal triangle", "sonny stitt, etc.", "sonny side up", 1957, "0", "", false));
        fakeSongList.add(new Song("after hours", "sonny stitt, etc.", "sonny side up", 1957, "0", "", true));
        fakeSongList.add(new Song("i know that you know", "sonny stitt, etc.", "sonny side up", 1957, "0", "", false));

        Playlist fakePlaylist = new Playlist("sick playlist", 0, fakeSongList);
        return fakePlaylist;
    }
}
