package space.collabify.android.collabify;

import android.util.Log;

import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import space.collabify.android.Endpoints;
import space.collabify.android.Json;
import space.collabify.android.collabify.api.CollabifyApi;
import space.collabify.android.collabify.api.CollabifyApiException;
import space.collabify.android.collabify.api.CollabifyService;
import space.collabify.android.managers.AppManager;
import space.collabify.android.models.Event;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;
import space.collabify.android.requests.EventsRequest;
import space.collabify.android.requests.PlaylistRequest;
import space.collabify.android.requests.UsersRequest;

/**
 * Created by ricardolopez on 3/22/15.
 */
public class CollabifyClient {
    private static final String TAG = CollabifyClient.class.getSimpleName();
    private static CollabifyClient instance;

    private CollabifyApi mCollabifyApi;

    private Event mJoinedEvent;
    private Playlist mEventPlaylist;

    private boolean eventUpdating;
    private boolean usersUpdating;
    private boolean playlistUpdating;

    private AppManager mAppManger = AppManager.getInstance();

    private CollabifyClient() {
        this.mCollabifyApi = new CollabifyApi();
    }

    public static CollabifyClient getInstance() {
        if (instance == null) {
            instance = new CollabifyClient();
        }
        return instance;
    }

    public CollabifyApi getCollabifyApi() {
        return mCollabifyApi;
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
        events.add(new Event("Whoops, something went wrong!", "9999", "", false));
        events.add(new Event("Please pull to refresh", "9999", "", false));
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
      //seems like the actual server communication for joining events is
      //taken care of
      mJoinedEvent = event;
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
        users.add(new User("Whoops, something went wrong!", "9999"));
        users.add(new User("Please pull to refresh", "9999"));
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

    public void getEventPlaylist(Callback<space.collabify.android.collabify.models.domain.Playlist> callback){
        try {
            mCollabifyApi.getEventPlaylist(mJoinedEvent.getId(), callback);
        } catch (CollabifyApiException e) {
            Log.d(TAG, "Unable to get event playlist:\n" + e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Upvotes a song. removes effect of a previous downvote if one
     * @param song the song to be upvoted
     */
    public void upvoteSong(Song song){
        if(song == null) {
            Log.w(TAG, "Tried to upvote null song");
            return;
        }
        song.upvote();
        //TODO: server stuff
        //need a vote endpoint?
    }

    /**
     * Removes a song added by the user, or if the user is the dj.
     * @param song the song to be deleted
     */
    public void deleteSong(Song song) {
        if(song == null){
            Log.w(TAG, "Tried to delete null song");
            return;
        }
        User user = mAppManger.getUser();

        //may not actually be necessary since the playlist will be deleted on the server, and
        // the new playlist will eventually be received.. but might update the ui faster
        if(user.getRole().isDJ() || song.wasAddedByUser()){
            mEventPlaylist.removeSong(song);
        }

        try {
            mCollabifyApi.removeSong(mJoinedEvent.getId(), song.getId());
        } catch (CollabifyApiException e) {
            Log.d(TAG, "Exception when trying to delete song from playlist");
            e.printStackTrace();
        }

    }

    /**
     * Downvote a song in the playlist.
     * @param song the song to be downvoted
     */
    public void downvoteSong(Song song) {
        if(song == null){
            Log.w(TAG, "Tried to downvote null song");
            return;
        }
        song.downvote();
        //TODO: server stuff
        //need a vote endpoint?
    }

    /**
     * Sets the users vote back to neutral (not upvote or downvote).
     * @param song song to clear the vote on
     */
    public void clearSongVote(Song song){
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
