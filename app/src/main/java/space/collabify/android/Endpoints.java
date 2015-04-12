package space.collabify.android;

/**
 * Created by andrew on 4/7/15.
 */
public class Endpoints {
    public static String VAR_EVENTID = ":eventId";
    public static String VAR_SONGID = ":songId";
    public static String PORT = "1337";

    public static String EVENTS = "http://collabify.space:" + PORT + "/events/";
    public static String EVENTUSERS = "http://collabify.space:" + PORT + "/events/" + VAR_EVENTID + "/users/";
    public static String PLAYLIST = "http://collabify.space:" + PORT + "/events/" + VAR_EVENTID + "/playlist/";
    public static String SONG = PLAYLIST + VAR_SONGID + "/";
    public static String USERS = "http://collabify.space:" + PORT + "/users";
}
