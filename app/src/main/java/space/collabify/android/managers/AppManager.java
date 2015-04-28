package space.collabify.android.managers;

import android.content.Context;
import android.util.Log;

import com.spotify.sdk.android.authentication.AuthenticationClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.collabify.api.CollabifyApi;
import space.collabify.android.collabify.api.CollabifyApiException;
import space.collabify.android.collabify.models.Converter;
import space.collabify.android.collabify.models.domain.Event;
import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.collabify.models.domain.Location;
import space.collabify.android.collabify.models.domain.UserSettings;
import space.collabify.android.collabify.models.domain.Vote;
import space.collabify.android.collabify.models.network.EventDO;
import space.collabify.android.collabify.models.network.EventRequestDO;
import space.collabify.android.collabify.models.network.RoleDO;
import space.collabify.android.collabify.models.network.SongRequestDO;
import space.collabify.android.collabify.models.network.UserDO;
import space.collabify.android.collabify.models.network.UserRequestDO;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Role;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;

/**
 * This class handles most activities that deal with networking and the app models. All callbacks
 * are null checked so it's fine to pass in null callbacks if you don't need to do anything.
 *
 * *NOTE* The callbacks that you pass to the methods should only be concerned with what
 * your activity or fragment or whatever called the method is doing. The methods will already
 * handle updating models. See the LoginScreenActivity.onActivityResult() method for an example.
 * The callback just handles hiding the progress bar and going to the next activity. This class
 * handles updating the user and setting the SpotifyApi accessToken.
 *
 * Created by ricardolopez on 4/15/15.
 */
public class AppManager {
    private static final AppManager instance = new AppManager();
    private static final String TAG = AppManager.class.getSimpleName();
    private static final String PRODUCT_PREMIUM = "premium";

    private SpotifyApi mSpotifyApi;
    private CollabifyApi mCollabifyApi;
    private SpotifyService mSpotifyService;

    private User mUser;
    private Event mEvent;
    private space.collabify.android.collabify.models.domain.Playlist mPlaylist;

    private boolean mEventUpdating = false;
    private boolean mUsersUpdating = false;
    private boolean mPlaylistUpdating = false;

    private int currentSong = -1;

    private android.location.Location mLastUserLocation;

    /**
     * Private constructor
     */
    private AppManager(){
        mCollabifyApi = new CollabifyApi();
        mSpotifyApi = new SpotifyApi();
    }

    /**
     * Gets instance of AppManager singleton
     *
     * @return instance of the app manager
     */
    public static AppManager getInstance(){
        return instance;
    }

    public boolean isEventUpdating() {
        return mEventUpdating;
    }

    public boolean isUsersUpdating() {
        return mUsersUpdating;
    }

    public boolean isPlaylistUpdating() {
        return mPlaylistUpdating;
    }


    public SpotifyService getSpotifyService(){
        return mSpotifyService;
    }


    public android.location.Location getLastKnownLocation() {
        return mLastUserLocation;
    }

    public void updateLocation(android.location.Location location){
        if(location != null){
            mLastUserLocation = location;
        }
    }

    /*
     * User
     */

    /**
     * Gets the current logged in User
     *
     * @return  the currently logged in User
     */
    public User getUser() {
        return mUser;
    }

    /**
     * Handles all the post Spotify login setup
     *
     * @param accessToken   the accessToken from spotify
     * @param callback  post login callback
     */
    public void login(final String accessToken, final CollabifyCallback<String> callback) {
        mSpotifyApi.setAccessToken(accessToken);
        mSpotifyService = mSpotifyApi.getService();

        // get the user information
        mSpotifyService.getMe(new Callback<kaaes.spotify.webapi.android.models.User>() {
            @Override
            public void success(kaaes.spotify.webapi.android.models.User user, Response response) {
                // set up the user
                mUser = new User(user.display_name, user.id, Role.NO_ROLE);
                mUser.setPremium(user.product.contains(PRODUCT_PREMIUM));
                mUser.setAccessToken(accessToken);
                mCollabifyApi.setCurrentUserId(mUser.getId());
                Log.i(TAG, "Display name: " + mUser.getName() + ", UserID: " + mUser.getId());

                // collabify login
                collabifyLogin(mUser, callback);

            }

            @Override
            public void failure(RetrofitError retrofitError) {
                // handle failure
                if (callback != null) {
                    callback.failure(retrofitError);
                }
            }
        });
    }

    /**
     * Logs the user into the collabify server. Called after spotify login.
     *
     * @param user
     * @param callback
     */
    private void collabifyLogin(User user, final CollabifyCallback<String> callback) {
        UserRequestDO userDO = new UserRequestDO();
        userDO.setName(user.getName());
        userDO.setSettings(new UserSettings());
        userDO.getSettings().setShowName(true);

        try {
            mCollabifyApi.addUser(userDO, new Callback<space.collabify.android.collabify.models.domain.User>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.User user, Response response) {

                    // update the user role
                    mUser.setRole(user.getRole());
                    mEvent = new Event();
                    mEvent.setEventId(user.getEventId());
                    // call the callback
                    if (callback != null) {
                        callback.success(user.getEventId(), response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                    // handle failure
                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            e.printStackTrace();
        }
    }

    /**
     * Handles logging the user out of our server
     *
     * Assume that logging out of spotify will always succeed. The callback will be used when
     * logging out of our server only.
     *
     * @param callback  post logout callback
     */
    public void logout(Context context, final CollabifyResponseCallback callback) {

        AuthenticationClient.logout(context);

        try {
            mCollabifyApi.logoutUser(mUser.getId(), new ResponseCallback() {
                @Override
                public void success(Response response) {
                    // handle success response
                    mUser.setRole(Role.NO_ROLE);
                    mCollabifyApi.setCurrentUserId(null);

                    // call callback success
                    if (callback != null) {
                        callback.success(response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // handle failure response

                    // call callback failure
                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            e.printStackTrace();
        }
    }

    /*
     * Users
     */

    /**
     * Loads the user list for the current event
     * @param callback
     */
    public void loadEventUsers(final CollabifyCallback<List<User>> callback) {
        try {
            mCollabifyApi.getEventUsers(mEvent.getEventId(), new Callback<List<UserDO>>() {
                @Override
                public void success(List<UserDO> userDOs, Response response) {

                    List<String> userIds = new ArrayList<String>();

                    for (UserDO userDO : userDOs) {
                        userIds.add(userDO.getUserId());
                    }

                    if (callback != null) {
                        callback.success(Converter.toUserList(userDOs), response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {


                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            e.printStackTrace();
        }
    }


    /*
     * Event
     */

    /**
     * Get the current event info
     * @return
     */
    public Event getEvent() {
        return mEvent;
    }

    /**
     *
     * @param latitude
     * @param longitude
     * @param callback
     */
    public void loadEvents(String latitude, String longitude, final Callback<List<space.collabify.android.models.Event>> callback) {
        if (latitude == null || longitude == null) {
            return;
        }

        mCollabifyApi.getEvents(latitude, longitude, new Callback<List<EventDO>>() {
            @Override
            public void success(List<EventDO> eventDOs, Response response) {

                if (callback != null) {
                    callback.success(Converter.toEventList(eventDOs), response);
                }
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                if (callback != null) {
                    callback.failure(retrofitError);
                }
            }
        });
    }

    /**
     * Handles joining an event on our server
     *
     * @param eventID
     * @param callback
     */
    public void joinEvent(final String eventID, final CollabifyCallback<space.collabify.android.models.Event> callback) {

        mEventUpdating = true;
        try {
            mCollabifyApi.joinEvent(eventID, new Callback<Event>() {
                @Override
                public void success(Event event, Response response) {
                    // do my stuff here
                    mEvent = new Event();
                    mEvent.setEventId(event.getEventId());
                    mEvent.setUserIds(event.getUserIds());
                    mEvent.setName(event.getName());
                    mEventUpdating = false;
                    mUser.setRole(Role.COLLABIFIER);
                    mPlaylist = event.getPlaylist();

                    // call callback
                    if (callback != null) {
                        callback.success(Converter.getAppEvent(event), response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // do my stuff here
                    mEventUpdating = false;

                    // call calback
                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            mEventUpdating = false;
            e.printStackTrace();
        }
    }

    /**
     * Handles creating an event on our server from an event object
     *
     * @param event
     * @param callback
     */
    public void createEvent(space.collabify.android.models.Event event, final CollabifyCallback<Event> callback) {

        mEventUpdating = true;
        // convert the event info to event do
        EventRequestDO eventDO = new EventRequestDO();
        EventSettings eventSettings = new EventSettings();

        eventSettings.setAllowVoting(event.isAllowVoting());
        eventSettings.setLocationRestricted(false);
        eventSettings.setPassword(event.getPassword());

        eventDO.setLocation(new Location());
        eventDO.getLocation().setLongitude(event.getLongitude());
        eventDO.getLocation().setLatitude(event.getLatitude());
        eventDO.setName(event.getName());
        eventDO.setSettings(eventSettings);

        try {
            mCollabifyApi.createEvent(eventDO, new Callback<Event>() {
                @Override
                public void success(Event event, Response response) {
                    // do my stuff here
                    mEvent = event;
                    mEventUpdating = false;
                    mUser.setRole(Role.DJ);

                    // call callback
                    if (callback != null) {
                        callback.success(event, response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // do my stuff here
                    mEventUpdating = false;

                    // call callback
                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            mEventUpdating = false;
            e.printStackTrace();
        }
    }

    /**
     * Handles the user leaving an event
     *
     * @param callback
     */
    public void leaveEvent(final CollabifyResponseCallback callback) {
        try {
            mCollabifyApi.leaveEvent(mEvent.getEventId(), new ResponseCallback() {
                @Override
                public void success(Response response) {
                    // handle success response
                    mEvent = null;
                    mPlaylist = null;
                    mUser.setRole(Role.NO_ROLE);

                    // call callback success
                    if (callback != null) {
                        callback.success(response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // handle failure response

                    // call callback failure
                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            e.printStackTrace();
        }
    }

    /**
     * Handles DJ ending an event
     *
     * @param callback
     */
    public void endEvent(final CollabifyResponseCallback callback) {
        try {
            mCollabifyApi.deleteEvent(mEvent.getEventId(), new ResponseCallback() {
                @Override
                public void success(Response response) {
                    mEvent = null;
                    mPlaylist = null;
                    mUser.setRole(Role.NO_ROLE);

                    // call callback success
                    if (callback != null) {
                        callback.success(response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    // call callback failure
                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            e.printStackTrace();
        }
    }

    /**
     * Handles DJ ending an event
     */
    public void endEvent() {
        try {
            mCollabifyApi.deleteEvent(mEvent.getEventId());
            mEvent = null;
        } catch (CollabifyApiException e) {
            e.printStackTrace();
        }
    }

    /*
     * Songs
     */

    /**
     * Moves a song down in the playlist on the server
     *
     * @param song
     * @param postion
     * @param updated
     * @param callback
     */
    public void moveSong(Song song, int postion, int updated, final CollabifyCallback<List<Song>> callback) {
      if (song != null) {
        // do server stuff here and on callback do this
        if (postion >= 0 && postion < mPlaylist.getSongs().size() && updated >= 0 && updated < mPlaylist.getSongs().size()) {
          mPlaylistUpdating = true;

          try {
            mCollabifyApi.reorderPlaylist(mEvent.getEventId(), postion, updated, new Callback<space.collabify.android.collabify.models.domain.Playlist>() {
              @Override
              public void success(space.collabify.android.collabify.models.domain.Playlist playlist, Response response) {
                mPlaylist = playlist;
                mPlaylistUpdating = false;

                // call callback success
                if (callback != null) {
                  callback.success(Converter.toPlaylist(playlist), response);
                }
              }

              @Override
              public void failure(RetrofitError retrofitError) {
                mPlaylistUpdating = false;

                // call calback failure
                if (callback != null) {
                  callback.failure(retrofitError);
                }
              }
            });
          } catch (CollabifyApiException e) {
            if (callback != null) {
              callback.exception(e);
            }
            mPlaylistUpdating = false;
            e.printStackTrace();
          }
        }
      }
    }

    /**
     * Clears the user's vote on the song on the server
     *
     * @param song
     * @param callback
     */
    public void clearSongVote(final Song song, final CollabifyResponseCallback callback) {
        if (song != null) {
            Vote vote = new Vote();
            vote.setDownvoted(false);
            vote.setUpvoted(false);
            voteOnSong(song, vote, callback);
        }
    }
    /**
     * Adds the user's upvote to the song on the server
     *
     * @param song
     * @param callback
     */
    public void upvoteSong(Song song, CollabifyResponseCallback callback) {
        if (song != null) {
            Vote vote = new Vote();
            vote.setDownvoted(false);
            vote.setUpvoted(true);
            voteOnSong(song, vote, callback);
        }
    }

    /**
     * Adds the user's downvote to the song on the server
     *
     * @param song
     * @param callback
     */
    public void downvoteSong(Song song, CollabifyResponseCallback callback) {
        if (song != null) {
            Vote vote = new Vote();
            vote.setDownvoted(true);
            vote.setUpvoted(false);
            voteOnSong(song, vote, callback);
        }
    }


    private void voteOnSong(final Song song, Vote vote,final CollabifyResponseCallback callback){
        try{
            mCollabifyApi.voteOnSong(mEvent.getEventId(), song.getId(), vote, new Callback<Vote>() {
                @Override
                public void success(Vote vote, Response response) {
                    updateSongVote(song, vote);
                    if(callback != null){
                        callback.success(response);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    if(callback != null){
                        callback.failure(error);
                    }
                }
            });
        }catch (CollabifyApiException ex){
            if(callback != null){
                callback.exception(ex);
            }
            ex.printStackTrace();
        }
    }

    private void updateSongVote(Song song, Vote vote){
        if(vote.isUpvoted()){
            song.upvote();
        }else if(vote.isDownvoted()){
            song.downvote();
        }else {
            song.clearVote();
        }
    }

    /*
     * Playlist
     */

    /**
     * TEMPORARY
     * gets the song from the current playlist based on the songId
     * @param songId
     * @return
     */
    public Song getSong(String songId) {
        for(space.collabify.android.collabify.models.domain.Song song: mPlaylist.getSongs()) {
            if (song.getSongId().equals(songId)){
                return Converter.toSong(song);
            }
        }
        return null;
    }

    /**
     * Loads the event playlist
     *
     * @param callback
     */
    public void loadEventPlaylist(final CollabifyCallback<List<Song>> callback) {

        mPlaylistUpdating = true;
        try {
            mCollabifyApi.getEventPlaylist(mEvent.getEventId(), new Callback<space.collabify.android.collabify.models.domain.Playlist>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.Playlist playlist, Response response) {
                    mPlaylist = playlist;
                    mPlaylistUpdating = false;

                    // call callback success
                    if (callback != null) {
                        callback.success(Converter.toPlaylist(playlist), response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    mPlaylistUpdating = false;

                    // call calback failure
                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            mPlaylistUpdating = false;
            e.printStackTrace();
        }
    }

    /**
     * Adds a song to the event playlist and updates the playlist
     *
     * @param song
     * @param callback
     */
    public void addSong(Song song, final CollabifyCallback<space.collabify.android.collabify.models.domain.Playlist> callback) {

        if (song == null) {
            return;
        }

        mPlaylistUpdating = true;

        SongRequestDO songDO = new SongRequestDO();
        songDO.setAlbum(song.getAlbum());
        songDO.setArtist(song.getArtist());
        songDO.setSongId(song.getId());
        songDO.setArtworkUrl(song.getArtwork());
        songDO.setTitle(song.getTitle());
        songDO.setYear(song.getYear());


        try {
            mCollabifyApi.addSong(mEvent.getEventId(), songDO, new Callback<space.collabify.android.collabify.models.domain.Playlist>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.Playlist playlist, Response response) {
                    mPlaylistUpdating = false;
                    mPlaylist = playlist;

                    if (callback != null) {
                        callback.success(playlist, response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    mPlaylistUpdating = false;

                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            e.printStackTrace();
            mPlaylistUpdating = false;
        }
    }

    /**
     * Removes a song from the event playlist
     *
     * @param songId
     * @param callback
     */
    public void removeSong(String songId, final CollabifyResponseCallback callback) {

        if (songId == null) {
            return;
        }

        try {
            mCollabifyApi.removeSong(mEvent.getEventId(), songId, new ResponseCallback() {
                @Override
                public void success(Response response) {

                    if (callback != null) {
                        callback.success(response);
                    }
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                    if (callback != null) {
                        callback.failure(retrofitError);
                    }
                }
            });
        } catch (CollabifyApiException e) {
            if (callback != null) {
                callback.exception(e);
            }
            e.printStackTrace();
        }
    }

    public void nextSong() {
        try {
            mCollabifyApi.endCurrentSong(mEvent.getEventId(), new Callback<space.collabify.android.collabify.models.domain.Playlist>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.Playlist playlist, Response response) {
                    mPlaylist = playlist;
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } catch (CollabifyApiException e) {
            e.printStackTrace();
        }
    }

    public Song getCurrentSong() {
        if (mPlaylist == null) {
            return null;
        }
        return Converter.toSong(mPlaylist.getCurrentSong());
    }

    /**
     * Handles changing a user's role at an event
     *
     * @param callback
     */
    public void changeUserRole(User user, String role, final CollabifyCallback callback) {
      try {
        RoleDO newRole = new RoleDO();
        newRole.setRole(role);
        mCollabifyApi.changeUserRole(mEvent.getEventId(), user.getId(), newRole, new Callback<space.collabify.android.collabify.models.domain.Role>() {
          @Override
          public void success(space.collabify.android.collabify.models.domain.Role newrole, Response response) {
            if (callback != null) {
              callback.success(newrole, response);
            }
          }

          @Override
          public void failure(RetrofitError retrofitError) {
            // call callback failure
            if (callback != null) {
              callback.failure(retrofitError);
            }
          }
        });
      } catch (CollabifyApiException e) {
        if (callback != null) {
          callback.exception(e);
        }
        e.printStackTrace();
      }
    }
}
