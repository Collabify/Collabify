package space.collabify.android.collabify.api;

import java.util.List;

import retrofit.Callback;
import retrofit.ResponseCallback;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import space.collabify.android.collabify.models.domain.Event;
import space.collabify.android.collabify.models.network.EventDO;
import space.collabify.android.collabify.models.network.EventRequestDO;
import space.collabify.android.collabify.models.network.UserDO;
import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.collabify.models.domain.Song;
import space.collabify.android.collabify.models.domain.User;

/**
 * Holds all the Collabify endpoints and uses Retrofit to return objects
 *
 * Created by ricardolopez on 4/10/15.
 */
public interface CollabifyService {

    public static final String USER_HEADER = "userid";
    public static final String LAT_HEADER = "latitude";
    public static final String LONG_HEADER = "longitude";

    public static final String USER_PATHVAL = "userId";
    public static final String USERS_URL = "users/";
    public static final String USER_URL = USERS_URL + "{" + USER_PATHVAL + "}/";

    public static final String EVENT_PATHVAL = "eventId";
    public static final String EVENTS_URL = "/events/";
    public static final String EVENT_URL = EVENTS_URL + "{" + EVENT_PATHVAL + "}/";
    public static final String EVENT_USERS_URL = EVENT_URL + "users/";
    public static final String EVENT_USER_URL = EVENT_USERS_URL + "{" + USER_PATHVAL + "}";
    public static final String EVENT_USER_ROLE_URL = EVENT_USER_URL + "role/";

    public static final String PLAYLIST_URL = EVENT_URL + "playlist/";
    public static final String SONG_PATHVAL = "songId";
    public static final String SONG_URL = PLAYLIST_URL + "{" + SONG_PATHVAL + "}/";


    /*--------------------------------------------------------------------------------------------*
     * User -------------------------------------------------------------------------------- User *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get user info from the server
     *
     * @param userId The user's spotify id
     * @return
     */
    @GET(USER_URL)
    public User getUser(@Path(USER_PATHVAL) String userId);

    /**
     * Get a user from the server
     *
     * @param userId
     * @param callback
     */
    @GET(USER_URL)
    public void getUser(@Path(USER_PATHVAL) String userId, Callback<User> callback);

    /**
     * Add a user to the server
     *
     * @param currentUserId
     * @param user
     * @return
     */
    @POST(USERS_URL)
    public User addUser(@Header(USER_HEADER) String currentUserId, @Body UserDO user);

    /**
     * Add a user to the server
     *
     * @param currentUserId
     * @param callback
     * @return
     */
    @POST(USERS_URL)
    public void addUser(@Header(USER_HEADER) String currentUserId, @Body UserDO user, Callback<User> callback);

    /**
     * Update the user's showName
     *
     * @param userId
     * @param showName
     * @return
     */
    @PUT(USER_URL)
    public User updateUser(@Path(USER_PATHVAL) String userId, @Body String showName);

    /**
     * Update the user's showName
     *
     * @param userId
     * @param showName
     * @param callback
     * @return
     */
    @PUT(USER_URL)
    public void updateUser(@Path(USER_PATHVAL) String userId, @Body String showName, Callback<User> callback);

    /**
     * Log out the user and remove him from the server
     *
     * @param userId
     */
    @DELETE(USER_URL)
    public void logoutUser(@Path(USER_PATHVAL) String userId);

    /**
     * Log out the user and remove him from the server
     *
     * @param userId
     * @param callback
     */
    @DELETE(USER_URL)
    public void logoutUser(@Path(USER_PATHVAL) String userId, ResponseCallback callback);


    /*--------------------------------------------------------------------------------------------*
     * Playlist ------------------------------------------------------------------------ Playlist *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @return
     */
    @GET(PLAYLIST_URL)
    public Playlist getEventPlaylist(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId);

    /**
     * Get an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param callback
     */
    @GET(PLAYLIST_URL)
    public void getEventPlaylist(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, Callback<Playlist> callback);

    /**
     * Add a song to an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param song
     * @return
     */
    @POST(PLAYLIST_URL)
    public Playlist addSong(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Body Song song);

    /**
     * Add a song to an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param song
     * @param callback
     */
    @POST(PLAYLIST_URL)
    public void addSong(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Body Song song, Callback<Playlist> callback);

    /**
     * Reorder an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param reorderedPlaylist
     * @return
     */
    @PUT(PLAYLIST_URL)
    public Playlist reorderPlaylist(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Body Playlist reorderedPlaylist);

    /**
     * Reorder an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param reorderedPlaylist
     * @param callback
     */
    @PUT(PLAYLIST_URL)
    public void reorderPlaylist(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Body Playlist reorderedPlaylist, Callback<Playlist> callback);

    /**
     * Remove a song from an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param songId
     */
    @DELETE(SONG_URL)
    public void removeSong(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Path(SONG_PATHVAL) String songId);

    /**
     * Remove a song from an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param songId
     * @param callback
     */
    @DELETE(SONG_URL)
    public void removeSong(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Path(SONG_PATHVAL) String songId, ResponseCallback callback);

    /*--------------------------------------------------------------------------------------------*
     * Event ------------------------------------------------------------------------------ Event *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get information on an event
     *
     * @param currentUserId
     * @param eventId
     * @return
     */
    @GET(EVENT_URL)
    public EventSettings getEventInfo(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId);

    /**
     * Get information on an event
     *
     * @param currentUserId
     * @param eventId
     * @param callback
     */
    @GET(EVENT_URL)
    public void getEventInfo(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, Callback<EventSettings> callback);

    /**
     * Get nearby events
     *
     * @param latitude
     * @param longitude
     * @return
     */
    @GET(EVENTS_URL)
    public List<EventDO> getEvents(@Header(LAT_HEADER) String latitude, @Header(LONG_HEADER) String longitude);

    /**
     * Get nearby events
     *
     * @param latitude
     * @param longitude
     * @param callback
     */
    @GET(EVENTS_URL)
    public void getEvents(@Header(LAT_HEADER) String latitude, @Header(LONG_HEADER) String longitude, Callback<List<EventDO>> callback);

    /**
     * Create a new event
     *
     * @param currentUserId
     * @param event
     * @return
     */
    @POST(EVENTS_URL)
    public Event createEvent(@Header(USER_HEADER) String currentUserId, @Body EventRequestDO event);

    /**
     * Create a new event
     *
     * @param currentUserId
     * @param event
     * @param callback
     */
    @POST(EVENTS_URL)
    public void createEvent(@Header(USER_HEADER) String currentUserId, @Body EventRequestDO event, Callback<Event> callback);

    /**
     * Update an events info
     *
     * @param currentUserId
     * @param eventId
     * @param eventInfo
     * @return
     */
    @PUT(EVENT_URL)
    public EventSettings updateEvent(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Body EventSettings eventInfo);

    /**
     * Update an events info
     *
     * @param currentUserId
     * @param eventId
     * @param eventInfo
     * @param callback
     */
    @PUT(EVENT_URL)
    public void updateEvent(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Body EventSettings eventInfo, Callback<EventSettings> callback);

    /**
     * Delete an event
     *
     * @param currentUserId
     * @param eventId
     */
    @DELETE(EVENT_URL)
    public void deleteEvent(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId);

    /**
     * Delete an event
     *
     * @param currentUserId
     * @param eventId
     * @param callback
     */
    @DELETE(EVENT_URL)
    public void deleteEvent(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, ResponseCallback callback);


    /*--------------------------------------------------------------------------------------------*
     * EventUser ---------------------------------------------------------------------- EventUser *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get a list of users that are currently attending an event
     *
     * @param currentUserId
     * @param eventId
     * @return
     */
    @GET(EVENT_USERS_URL)
    public List<UserDO> getEventUsers(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId);

    /**
     * Get a list of users that are currently attending an event
     *
     * @param currentUserId
     * @param eventId
     * @param callback
     */
    @GET(EVENT_USERS_URL)
    public void getEventUsers(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, Callback<List<UserDO>> callback);

    /**
     * Join an event
     *
     * @param currentUserId
     * @param eventId
     * @return
     */
    @POST(EVENT_USERS_URL)
    public UserDO joinEvent(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId);

    /**
     * Join an event
     *
     * @param currentUserId
     * @param eventId
     * @param callback
     */
    @POST(EVENT_USERS_URL)
    public void joinEvent(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, Callback<UserDO> callback);

    /**
     * Leave an event
     *
     * @param eventId
     * @param userId
     */
    @DELETE(EVENT_USER_URL)
    public void leaveEvent(@Path(EVENT_PATHVAL) String eventId, @Path(USER_PATHVAL) String userId);

    /**
     * Leave an event
     *
     * @param eventId
     * @param userId
     * @param callback
     */
    @DELETE(EVENT_USER_URL)
    public void leaveEvent(@Path(EVENT_PATHVAL) String eventId, @Path(USER_PATHVAL) String userId, ResponseCallback callback);

    /**
     * Change a user's role in an event
     *
     * @param currentUserId
     * @param eventId
     * @param userId
     * @param role
     * @return
     */
    @PUT(EVENT_USER_ROLE_URL)
    public String changeUserRole(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Path(USER_PATHVAL) String userId, @Body String role);

    /**
     * Change a user's role in an event
     *
     * @param currentUserId
     * @param eventId
     * @param userId
     * @param role
     * @param callback
     */
    @PUT(EVENT_USER_ROLE_URL)
    public void changeUserRole(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Path(USER_PATHVAL) String userId, @Body String role, Callback<String> callback);
}
