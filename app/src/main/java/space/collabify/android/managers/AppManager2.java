package space.collabify.android.managers;

import android.content.Context;

import com.spotify.sdk.android.authentication.AuthenticationClient;

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
import space.collabify.android.collabify.models.network.EventDO;
import space.collabify.android.collabify.models.network.EventRequestDO;
import space.collabify.android.collabify.models.network.SongRequestDO;
import space.collabify.android.collabify.models.network.UserDO;
import space.collabify.android.models.Playlist;
import space.collabify.android.models.Role;
import space.collabify.android.models.Song;
import space.collabify.android.models.User;

/**
 * This class handles most activities that deal with networking and the app models.
 * *NOTE* The callbacks that you pass to the methods should only be concerned with what
 * your activity or fragment or whatever called the method is doing. The methods will already
 * handle updating models. See the LoginScreenActivity.onActivityResult() method for an example.
 * The callback just handles hiding the progress bar and going to the next activity. This class
 * handles updating the user and setting the SpotifyApi accessToken.
 *
 * Created by ricardolopez on 4/15/15.
 */
public class AppManager2 {
    private static final AppManager2 instance = new AppManager2();

    private static final String PRODUCT_PREMIUM = "premium";

    private SpotifyApi mSpotifyApi;
    private CollabifyApi mCollabifyApi;
    private SpotifyService mSpotifyService;

    private User mUser;
    private Event mEvent;
    private Playlist mPlaylist;

    private boolean mEventUpdating = false;
    private boolean mUsersUpdating = false;
    private boolean mPlaylistUpdating = false;

    /**
     * Private constructor
     */
    private AppManager2(){
        mCollabifyApi = new CollabifyApi();
        mSpotifyApi = new SpotifyApi();
    }

    /**
     * Gets instance of AppManager singleton
     *
     * @return instance of the app manager
     */
    public static AppManager2 getInstance(){
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
    public void login(final String accessToken, final ResponseCallback callback) {
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

                // call the callback
                if (callback != null) {
                    callback.success(response);
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
    public void joinEvent(String eventID, final CollabifyCallback<space.collabify.android.collabify.models.domain.User> callback) {

        mEventUpdating = true;
        try {
            mCollabifyApi.joinEvent(eventID, new Callback<space.collabify.android.collabify.models.domain.User>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.User user, Response response) {
                    // do my stuff here
                    mEvent = new Event();
                    mEvent.setEventId(user.getEventId());
                    mEventUpdating = false;

                    // call callback
                    if (callback != null) {
                        callback.success(user, response);
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

        // TODO: eventDO.setLocation()
        eventDO.setName(event.getName());
        eventDO.setSettings(eventSettings);

        try {
            mCollabifyApi.createEvent(eventDO, new Callback<Event>() {
                @Override
                public void success(Event event, Response response) {
                    // do my stuff here
                    mEvent = event;
                    mEventUpdating = false;

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

    public void endEvent(final CollabifyResponseCallback callback) {
        try {
            mCollabifyApi.deleteEvent(mEvent.getEventId(), new ResponseCallback() {
                @Override
                public void success(Response response) {
                    mEvent = null;
                    mPlaylist = null;

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

    /*
     * Songs
     */

    /**
     * Adds the user's upvote to the song on the server
     *
     * @param song
     * @param callback
     */
    public void upvoteSong(Song song, CollabifyResponseCallback callback) {
        if (song != null) {
            // do server stuff here and on callback do this
            song.downvote();
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
            // do server stuff here and on callback do this
            song.downvote();
        }
    }

    /**
     * Clears the user's vote on the song on the server
     *
     * @param song
     * @param callback
     */
    public void clearSongVote(Song song, CollabifyResponseCallback callback) {
        if (song != null) {
            // do server stuff here and on callback do this
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
        for(Song song: mPlaylist.getmList()) {
            if (song.getId().equals(songId)){
                return song;
            }
        }
        return null;
    }

    /**
     * Loads the event playlist
     *
     * @param callback
     */
    public void loadEventPlaylist(final CollabifyCallback<Playlist> callback) {

        mPlaylistUpdating = true;
        try {
            mCollabifyApi.getEventPlaylist(mEvent.getEventId(), new Callback<space.collabify.android.collabify.models.domain.Playlist>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.Playlist playlist, Response response) {
                    mPlaylist = Converter.getAppPlaylist(playlist);

                    mPlaylistUpdating = false;

                    // call callback success
                    if (callback != null) {
                        callback.success(mPlaylist, response);
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
    public void addSong(Song song, final CollabifyCallback<Playlist> callback) {

        if (song == null) {
            return;
        }

        mPlaylistUpdating = true;

        SongRequestDO songDO = new SongRequestDO();
        songDO.setAlbum(song.getAlbum());
        songDO.setArtist(song.getArtist());
        songDO.setArtworkUrl(song.getArtwork());
        songDO.setTitle(song.getTitle());
        songDO.setYear(song.getYear());


        try {
            mCollabifyApi.addSong(mEvent.getEventId(), songDO, new Callback<space.collabify.android.collabify.models.domain.Playlist>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.Playlist playlist, Response response) {
                    mPlaylistUpdating = false;
                    mPlaylist = Converter.getAppPlaylist(playlist);

                    if (callback != null) {
                        callback.success(mPlaylist, response);
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
     * @param song
     * @param callback
     */
    public void removeSong(Song song, final CollabifyResponseCallback callback) {

        if (song == null) {
            return;
        }

        try {
            mCollabifyApi.removeSong(mEvent.getEventId(), song.getId(), new ResponseCallback() {
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
}
