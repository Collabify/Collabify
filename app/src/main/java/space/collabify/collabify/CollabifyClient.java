package space.collabify.collabify;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.User;

/**
 * Created by ricardolopez on 3/22/15.
 */
public class CollabifyClient {

  private static CollabifyClient instance;

  private boolean eventUpdating;
  private boolean usersUpdating;

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

    // TODO: On successful return of event list
    eventUpdating = false;

    return events;
  }

  public boolean isEventUpdating() {
    return eventUpdating;
  }

  public void joinEvent(Event event, User user){
    //TODO: implementation
  }

  /**
   * Queries the server for users
   * @param request query information
   * @return list of events based off of request param
   */
  public List<User> getUsers(LoadUsersRequest request){
    //TODO fill in with actual server stuff

    usersUpdating = true;

    //fake some server delay
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    ArrayList<User> users = new ArrayList<>();
    users.add(new User("Best Singers Ever!!!", 0));
    users.add(new User("Katy Perry", 1));
    users.add(new User("Taylor Swift", 2));
    users.add(new User("Miley Cyrus", 3));
    users.add(new User("Spice Girls", 4));
    users.add(new User("Beyonce", 5));
    users.add(new User("Madonna", 6));
    users.add(new User("Brittney Spears", 7));
    users.add(new User("Lady Gaga", 8));
    users.add(new User("Avril Lavigne", 9));
    users.add(new User("P!nk", 10));
    users.add(new User("Alanis Morissette", 11));

    usersUpdating = false;

    return users;
  }

  public boolean isUsersUpdating() {
    return usersUpdating;
  }
}
