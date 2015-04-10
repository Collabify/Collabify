package space.collabify.collabify.requests;

import android.location.Location;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import space.collabify.collabify.Endpoints;
import space.collabify.collabify.Json;
import space.collabify.collabify.models.Event;

/**
 * This file was born on March 20, at 19:50
 * Includes whatever data is needed to request a list of events from the server
 */
public class EventsRequest {
    public Location userLocation;

  public ArrayList<Event> get() {
    ArrayList<Event> events = new ArrayList<>();

    JSONArray jArray = Json.getJsonArray(
      Endpoints.EVENTS,
      new String[] {"userid", "latitude", "longitude"},
      new String[] {"user", "10", "10"}
    );

    if (jArray != null) {
      for (int i = 0; i < jArray.length(); i++) {
        try {
          JSONObject oneObject = jArray.getJSONObject(i);
          // Pulling items from the array
          String name = oneObject.getString("name");
          String id = oneObject.getString("eventId");
          JSONObject settings = oneObject.getJSONObject("settings");

          String password;
          if (settings.isNull("password")) {
            password = "";
          } else {
            password = settings.getString("password");
          }
          Boolean allowVoting = settings.getBoolean("allowVoting");

          events.add(new Event(name, id, password, allowVoting));
        } catch (Exception e) {
          events = null;
          break;
        }
      }
    } else {
      events = null;
    }

    return events;
  }
}
