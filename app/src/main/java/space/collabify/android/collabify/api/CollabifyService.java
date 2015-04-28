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
import space.collabify.android.collabify.models.domain.Role;
import space.collabify.android.collabify.models.domain.UserSettings;
import space.collabify.android.collabify.models.domain.Vote;
import space.collabify.android.collabify.models.network.EventDO;
import space.collabify.android.collabify.models.network.EventRequestDO;
import space.collabify.android.collabify.models.network.RoleDO;
import space.collabify.android.collabify.models.network.SongRequestDO;
import space.collabify.android.collabify.models.network.UserDO;
import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.collabify.models.domain.Playlist;
import space.collabify.android.collabify.models.domain.Song;
import space.collabify.android.collabify.models.domain.User;
import space.collabify.android.collabify.models.network.UserRequestDO;
import space.collabify.android.managers.CollabifyResponseCallback;

/**
 * Holds all the Collabify endpoints and uses Retrofit to return objects
 *
 * Created by ricardolopez on 4/10/15.
 */
public interface CollabifyService {

    public static final String USER_HEADER = "userid";
    public static final String LAT_HEADER = "latitude";
    public static final String LONG_HEADER = "longitude";
    public static final String OLD_INDEX_HEADER = "oldindex";
    public static final String NEW_INDEX_HEADER = "newindex";

    public static final String USER_PATHVAL = "userId";
    public static final String USERS_URL = "/users/";
    public static final String USER_URL = USERS_URL + "{" + USER_PATHVAL + "}/";
    public static final String USER_SETTINGS_URL = USER_URL + "settings/";

    public static final String EVENT_PATHVAL = "eventId";
    public static final String EVENTS_URL = "/events/";
    public static final String EVENT_URL = EVENTS_URL + "{" + EVENT_PATHVAL + "}/";
    public static final String EVENT_USERS_URL = EVENT_URL + "users/";
    public static final String EVENT_USER_URL = EVENT_USERS_URL + "{" + USER_PATHVAL + "}";
    public static final String EVENT_USER_ROLE_URL = EVENT_USER_URL + "/role/";
    public static final String EVENT_SETTINGS_URL = EVENT_URL + "settings/";

    public static final String PLAYLIST_URL = EVENT_URL + "playlist/";
    public static final String PLAYLIST_CURRENT_SONG_URL = EVENT_URL + "playlist/currentSong/";
    public static final String PLAYLIST_SONGS_URL = EVENT_URL + "playlist/songs/";
    public static final String SONG_PATHVAL = "songId";
    public static final String SONG_URL = PLAYLIST_SONGS_URL + "{" + SONG_PATHVAL + "}/";
    public static final String SONG_VOTE_URL = SONG_URL + "votes/{" + USER_PATHVAL + "}/";


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
    public User addUser(@Header(USER_HEADER) String currentUserId, @Body UserRequestDO user);

    /**
     * Add a user to the server
     *
     * @param currentUserId
     * @param callback
     * @return
     */
    @POST(USERS_URL)
    public void addUser(@Header(USER_HEADER) String currentUserId, @Body UserRequestDO user, Callback<User> callback);

    /**
     * Update the user's showName
     *
     * @param userId
     * @param showName
     * @return
     */
    @PUT(USER_SETTINGS_URL)
    public UserSettings updateUser(@Path(USER_PATHVAL) String userId, @Body String showName);

    /**
     * Update the user's showName
     *
     * @param userId
     * @param showName
     * @param callback
     * @return
     */
    @PUT(USER_SETTINGS_URL)
    public void updateUser(@Path(USER_PATHVAL) String userId, @Body String showName, Callback<UserSettings> callback);

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
     * Reorder an event's playlist
     *
     * @param eventId
     * @param oldIndex
     * @param newIndex
     * @return
     */
    @PUT(PLAYLIST_SONGS_URL)
    public Playlist reorderPlaylist(@Path(EVENT_PATHVAL) String eventId, @Header(OLD_INDEX_HEADER) int oldIndex, @Header(NEW_INDEX_HEADER) int newIndex);

    /**
     * Reorder an event's playlist
     *
     * @param eventId
     * @param oldIndex
     * @param newIndex
     * @param callback
     */
    @PUT(PLAYLIST_SONGS_URL)
    public void reorderPlaylist(@Path(EVENT_PATHVAL) String eventId, @Header(OLD_INDEX_HEADER) int oldIndex, @Header(NEW_INDEX_HEADER) int newIndex, Callback<Playlist> callback);

    /*--------------------------------------------------------------------------------------------*
     * Songs ------------------------------------------------------------------------------ Songs *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Place vote on a song.
     * @param mCurrentUserId
     * @param eventId
     * @param songId
     * @return
     */
    @PUT(SONG_VOTE_URL)
    public Vote voteOnSong(@Path(EVENT_PATHVAL) String eventId,@Path(SONG_PATHVAL) String songId, @Path(USER_PATHVAL) String mCurrentUserId, @Body Vote vote );


    /**
     * Place vote on a song.
     * @param mCurrentUserId
     * @param eventId
     * @param songId
     * @return
     */
    @PUT(SONG_VOTE_URL)
    public void voteOnSong(@Path(EVENT_PATHVAL) String eventId,@Path(SONG_PATHVAL) String songId, @Path(USER_PATHVAL) String mCurrentUserId, @Body Vote vote, Callback<Vote> callback);

    /**
     * Add a song to an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param song
     * @return
     */
    @POST(PLAYLIST_SONGS_URL)
    public Playlist addSong(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Body SongRequestDO song);

    /**
     * Add a song to an event's playlist
     *
     * @param currentUserId
     * @param eventId
     * @param song
     * @param callback
     */
    @POST(PLAYLIST_SONGS_URL)
    public void addSong(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, @Body SongRequestDO song, Callback<Playlist> callback);

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

    /**
     * End the current song and advance the queue to the next song (which is already on deck) and
     * get the updated playlist
     *
     * @param currentUserId
     * @param eventId
     * @return
     */
    @DELETE(PLAYLIST_CURRENT_SONG_URL)
    public Playlist endCurrentSong(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId);

    /**
     * End the current song and advance the queue to the next song (which is already on deck) and
     * get the updated playlist
     *
     * @param currentUserId
     * @param eventId
     * @param callback
     * @return
     */
    @DELETE(PLAYLIST_CURRENT_SONG_URL)
    public void endCurrentSong(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, Callback<Playlist> callback);

    /*--------------------------------------------------------------------------------------------*
     * Event ------------------------------------------------------------------------------ Event *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get information on an event
     *
     * @param eventId
     * @return
     */
    @GET(EVENT_SETTINGS_URL)
    public EventSettings getEventInfo(@Path(EVENT_PATHVAL) String eventId);

    /**
     * Get information on an event
     *
     * @param eventId
     * @param callback
     */
    @GET(EVENT_SETTINGS_URL)
    public void getEventInfo(@Path(EVENT_PATHVAL) String eventId, Callback<EventSettings> callback);

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
     * @param eventId
     * @param eventInfo
     * @return
     */
    @PUT(EVENT_SETTINGS_URL)
    public EventSettings updateEvent(@Path(EVENT_PATHVAL) String eventId, @Body EventSettings eventInfo);

    /**
     * Update an events info
     *
     * @param eventId
     * @param eventInfo
     * @param callback
     */
    @PUT(EVENT_SETTINGS_URL)
    public void updateEvent(@Path(EVENT_PATHVAL) String eventId, @Body EventSettings eventInfo, Callback<EventSettings> callback);

    /**
     * Delete an event
     *
     * @param eventId
     */
    @DELETE(EVENT_URL)
    public void deleteEvent(@Path(EVENT_PATHVAL) String eventId);

    /**
     * Delete an event
     *
     * @param eventId
     * @param callback
     */
    @DELETE(EVENT_URL)
    public void deleteEvent(@Path(EVENT_PATHVAL) String eventId, ResponseCallback callback);


    /*--------------------------------------------------------------------------------------------*
     * EventUser ---------------------------------------------------------------------- EventUser *
     *--------------------------------------------------------------------------------------------*/

    /**
     * Get a list of users that are currently attending an event
     *
     * @param eventId
     * @return
     */
    @GET(EVENT_USERS_URL)
    public List<UserDO> getEventUsers(@Path(EVENT_PATHVAL) String eventId);

    /**
     * Get a list of users that are currently attending an event
     *
     * @param eventId
     * @param callback
     */
    @GET(EVENT_USERS_URL)
    public void getEventUsers(@Path(EVENT_PATHVAL) String eventId, Callback<List<UserDO>> callback);

    /**
     * Join an event
     *
     * @param currentUserId
     * @param eventId
     * @return
     */
    @POST(EVENT_USERS_URL)
    public Event joinEvent(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId);

    /**
     * Join an event
     *
     * @param currentUserId
     * @param eventId
     * @param callback
     */
    @POST(EVENT_USERS_URL)
    public void joinEvent(@Header(USER_HEADER) String currentUserId, @Path(EVENT_PATHVAL) String eventId, Callback<Event> callback);

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
     * @param eventId
     * @param userId
     * @param role
     * @return
     */
    @PUT(EVENT_USER_ROLE_URL)
    public Role changeUserRole(@Path(EVENT_PATHVAL) String eventId, @Path(USER_PATHVAL) String userId, @Body String role);

    /**
     * Change a user's role in an event
     *
     * @param eventId
     * @param userId
     * @param role
     * @param callback
     */
    @PUT(EVENT_USER_ROLE_URL)
    public void changeUserRole(@Path(EVENT_PATHVAL) String eventId, @Path(USER_PATHVAL) String userId, @Body RoleDO role, Callback<Role> callback);

}
