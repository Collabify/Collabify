package space.collabify.collabify;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.User;
import space.collabify.collabify.Json;

/**
 * Created by ricardolopez on 3/22/15.
 */
public class CollabifyClient {

  private static CollabifyClient instance;

  private User currentUser;
  private Event currentEvent;

  private boolean eventUpdating;
  private boolean usersUpdating;

  // TODO: Find a better place for endpoints
  private String EVENTS = "http://collabify.space:1337/events/";
  private String USERS = "http://collabify.space:1337/events/:eventId/users/";

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
    eventUpdating = true;

    ArrayList<Event> events = new ArrayList<>();

    // Get json data
    JSONArray jArray = Json.getJsonArray(EVENTS);
    if (jArray != null) {
      for (int i = 0; i < jArray.length(); i++) {
        try {
          JSONObject oneObject = jArray.getJSONObject(i);
          // Pulling items from the array
          String name = oneObject.getString("name");
          int id = oneObject.getInt("eventId");
          JSONObject settings = oneObject.getJSONObject("settings");
          String password = settings.getString("password");
          Boolean passwordProtected = !settings.isNull("password");
          Boolean allowVoting = settings.getBoolean("allowVoting");
          events.add(new Event(name, id, passwordProtected, password, allowVoting));
        } catch (Exception e) {
          events.add(new Event("Whoops, something went wrong!", 0, false, null, false));
          events.add(new Event("Please pull to refresh", 0, false, null, false));
          break;
        }
      }
      eventUpdating = false;
    } else {
      events.add(new Event("Whoops, something went wrong!", 0, false, null, false));
      events.add(new Event("Please pull to refresh", 0, false, null, false));
    }

    // TODO: On successful return of event list
    return events;
  }

  public boolean isEventUpdating() {
    return eventUpdating;
  }

  public void joinEvent(Event event, User user){
    currentEvent = event;
    currentUser = user;
  }

  /**
   * Queries the server for users
   * @param request query information
   * @return list of events based off of request param
   */
  public List<User> getUsers(LoadUsersRequest request){
    //TODO fill in with actual server stuff

    usersUpdating = true;

    ArrayList<User> users = new ArrayList<>();

    // Get json data
    String eventUsers = USERS.replace(":eventId", String.valueOf(currentEvent.getId()));
    JSONArray jArray = Json.getJsonArray(eventUsers);
    if (jArray != null) {
      for (int i = 0; i < jArray.length(); i++) {
        try {
          JSONObject oneObject = jArray.getJSONObject(i);
          // Pulling items from the array
          String name = oneObject.getString("name");
          int id = oneObject.getInt("userId");
          users.add(new User(name, id));
        } catch (Exception e) {
          users.add(new User("Whoops, something went wrong!", 0));
          users.add(new User("Please pull to refresh", 0));
          break;
        }
      }
      usersUpdating = false;
    } else {
      users.add(new User("Whoops, something went wrong!", 0));
      users.add(new User("Please pull to refresh", 0));
    }

    return users;
  }

  public boolean isUsersUpdating() {
    return usersUpdating;
  }

}
