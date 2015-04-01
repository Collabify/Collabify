package space.collabify.collabify;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;
import space.collabify.collabify.Json;

/**
 * Created by ricardolopez on 3/22/15.
 */
public class CollabifyClient {
    private static final String TAG = CollabifyClient.class.getSimpleName();
    private static CollabifyClient instance;

    private Event mJoinedEvent;
    private Playlist mEventPlaylist;

    private boolean eventUpdating;
    private boolean usersUpdating;

    // TODO: Find a better place for endpoints
    private String EVENTS = "http://collabify.space/event.json";

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
     *
     * @param request query information
     * @return list of events based off of request param
     */
    public List<Event> getEvents(LoadEventsRequest request) {
        //TODO fill in with actual server stuff
        eventUpdating = true;

        ArrayList<Event> events = new ArrayList<>();

        // Get json data
        JSONArray jArray = Json.getJsonArray("events", EVENTS);
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    JSONObject oneObject = jArray.getJSONObject(i);
                    // Pulling items from the array
                    String name = oneObject.getString("name");
                    Boolean passwordProtected = oneObject.getBoolean("passwordProtected");
                    String password = oneObject.getString("password");
                    events.add(new Event(name, passwordProtected, password));
                } catch (Exception e) {
                    events.add(new Event("Whoops, something went wrong!", false, null));
                    events.add(new Event("Please pull to refresh", false, null));
                }
            }
            eventUpdating = false;
        } else {
            events.add(new Event("Whoops, something went wrong!", false, null));
            events.add(new Event("Please pull to refresh", false, null));
        }

        // TODO: On successful return of event list
        return events;
    }

    public boolean isEventUpdating() {
        return eventUpdating;
    }

    public void joinEvent(Event event, User user) {
        //TODO: implementation
    }

    /**
     * Queries the server for users
     *
     * @param request query information
     * @return list of events based off of request param
     */
    public List<User> getUsers(LoadUsersRequest request) {
        //TODO fill in with actual server stuff

        usersUpdating = true;

        //fake some server delay
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Best Singers Ever!!!", 0));
        users.add(new User("Katy Perry", 1));
        users.add(new User("Taylor Swift", 2));
        users.add(new User("Miley Cyrus", 3));
        users.add(new User("Spice Girls", 4));
        users.add(new User("Beyonce", 5));
        users.add(new User("Madonna", 6));
        users.add(new User("Brittney Spears", 7));
        users.add(new User("Lady Gaga", 8));
        users.add(new User("Avril Lavigne", 9));
        users.add(new User("P!nk", 10));
        users.add(new User("Alanis Morissette", 11));

        usersUpdating = false;

        return users;
    }

    public boolean isUsersUpdating() {
        return usersUpdating;
    /**
     * Register a user to an event
     * @param event
     * @param user
     */
    public void joinEvent(Event event, User user){
        //TODO: implementation
    }


    /**
     * Gets the event playlist, the most recent one from the server.
     * @return playlist of songs
     */
    public Playlist getEventPlaylist(){
        //TODO: actual server stuff to get the playlist
        if(mEventPlaylist == null){
            ArrayList<Song> fakeSongList = new ArrayList<>();
            fakeSongList.add(new Song("on the sunny side of the street", "sonny stitt, etc.", "sonny side up", 1957, "0", "", false));
            fakeSongList.add(new Song("the eternal triangle", "sonny stitt, etc.", "sonny side up", 1957, "1", "", false));
            fakeSongList.add(new Song("after hours", "sonny stitt, etc.", "sonny side up", 1957, "2", "", true));
            fakeSongList.add(new Song("i know that you know", "sonny stitt, etc.", "sonny side up", 1957, "3", "", false));
            fakeSongList.add(new Song("a reaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaly long entry", "random", "sonny side up", 1957, "5", "", false));

            mEventPlaylist = new Playlist("sick playlist", 0, fakeSongList);
        }
        return mEventPlaylist;
    }

    /**
     * Upvotes a song. removes effect of a previous downvote if one
     * @param user the user that is upvoting the song
     * @param song the song to be upvoted
     */
    public void upvoteSong(User user, Song song){
        if(song == null) {
            Log.w(TAG, "Tried to upvote null song");
            return;
        }
        //TODO: server stuff
    }

    /**
     * Removes a song added by the user, or if the user is the dj.
     * @param user user requesting the delete
     * @param song the song to be deleted
     */
    public void deleteSong(User user, Song song) {
        if(song == null){
            Log.w(TAG, "Tried to delete null song");
            return;
        }
        //TODO: actual server stuff
        if(user.getRole().isDJ() || song.wasAddedByUser()){
            mEventPlaylist.removeSong(song);
        }
    }

    /**
     * Downvote a song in the playlist.
     * @param user the user downvoting
     * @param song the song to be downvoted
     */
    public void downvoteSong(User user, Song song) {
        if(song == null){
            Log.w(TAG, "Tried to downvote null song");
            return;
        }
        //TODO: server stuff
    }

    /**
     * Sets the users vote back to neutral (not upvote or downvote).
     * @param user user to clear the vote from
     * @param song song to clear the vote on
     */
    public void clearSongVote(User user, Song song){
        if(song == null) {
            Log.w(TAG, "Tried to clear null song vote");
            return;
        }
        //TODO: server stuff
    }


    /**
     * Searches for the song in the current event playlist identified by songId, added by user
     * @param songId unique id of the song
     * @return song if found, otherwise null
     */
    public Song getSongById(String songId){
        //can improve performance here by doing smarter search
        for(Song song: mEventPlaylist.getmList()){
            if(song.getId().equalsIgnoreCase(songId)){
                return song;
            }
        }
        return null;
    }
}
