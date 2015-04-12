package space.collabify.android;

/**
 * Created by andrew on 4/7/15.
 */
public class Endpoints {
  public static String EVENTS = "http://collabify.space:" + 1337 + "/events/";
  public static String EVENTUSERS = "http://collabify.space:" + 1337 + "/events/:eventId/users/";
  public static String PLAYLIST = "http://collabify.space:" + 1337 + "/events/:eventId/playlist/";
  public static String USERS = "http://collabify.space:" + 1337 + "/users";
}
