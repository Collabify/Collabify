package space.collabify.collabify;

import android.util.Log;

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
    private static final String TAG = CollabifyClient.class.getSimpleName();
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
        ArrayList<Song> fakeSongList = new ArrayList<>();
        fakeSongList.add(new Song("on the sunny side of the street", "sonny stitt, etc.", "sonny side up", 1957, "0", "", false));
        fakeSongList.add(new Song("the eternal triangle", "sonny stitt, etc.", "sonny side up", 1957, "0", "", false));
        fakeSongList.add(new Song("after hours", "sonny stitt, etc.", "sonny side up", 1957, "0", "", true));
        fakeSongList.add(new Song("i know that you know", "sonny stitt, etc.", "sonny side up", 1957, "0", "", false));
        fakeSongList.add(new Song("a reaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaly long entry", "random", "sonny side up", 1957, "0", "", false));


        Playlist fakePlaylist = new Playlist("sick playlist", 0, fakeSongList);
        return fakePlaylist;
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
            if(song.getId().equals(songId) && song.wasAddedByUser()){
                return song;
            }
        }
        return null;
    }
}
