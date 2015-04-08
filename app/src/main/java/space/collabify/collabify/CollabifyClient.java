package space.collabify.collabify;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.managers.AppManager;
import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;
import space.collabify.collabify.requests.EventsRequest;
import space.collabify.collabify.requests.PlaylistRequest;
import space.collabify.collabify.requests.UsersRequest;

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
    private boolean playlistUpdating;

    private AppManager mAppManger = AppManager.getInstance();

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
    public List<Event> getEvents(EventsRequest request){
      eventUpdating = true;
      ArrayList<Event> events = request.get();

      if (events == null) {
        events = new ArrayList<>();
        events.add(new Event("Whoops, something went wrong!", 9999, "", false, false));
        events.add(new Event("Please pull to refresh", 9999, "", false, false));
      } else {
        eventUpdating = false;
      }

      return events;
    }

    public boolean isEventUpdating() {
      return eventUpdating;
    }

    /**
     * Register a user to an event (server-side)
     * @param event Event to join
     * @param user Current user
     */
    public void joinEvent(Event event, User user){
      //TODO: implementation
    }

  /**
   * Create a new event (server-side)
   * @param event Event to create
   * @param user Current user
   */
    public void createEvent(Event event, User user) {
      // TODO: implementation
    }

    /**
     * Queries the server for users
     * @param request query information
     * @return list of events based off of request param
     */
    public List<User> getUsers(UsersRequest request){
      usersUpdating = true;
      ArrayList<User> users = request.get(mAppManger.getEvent().getId());

      if (users == null) {
        users = new ArrayList<>();
        users.add(new User("Whoops, something went wrong!", 9999));
        users.add(new User("Please pull to refresh", 9999));
      } else {
        usersUpdating = false;
      }

      return users;
    }

    public boolean isUsersUpdating() {
      return usersUpdating;
    }


    /**
     * Gets the event playlist, the most recent one from the server.
     * @return playlist of songs
     */
    public Playlist getEventPlaylist(PlaylistRequest request){

        if(mEventPlaylist == null) {
          mEventPlaylist = request.get(mAppManger.getEvent().getId());
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
        song.upvote();
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
        song.downvote();
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
        song.clearVote();
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

    public boolean isPlaylistUpdating() {
      return playlistUpdating;
    }

    public void resetPlaylist() {
      mEventPlaylist = null;
    }
}
