package space.collabify.android.requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import space.collabify.android.Endpoints;
import space.collabify.android.Json;
import space.collabify.android.models.User;

/**
 * This file was born on March 20, at 19:50
 * Includes whatever data is needed to request a list of events from the server
 */
public class UsersRequest {

  public ArrayList<User> get(String eventId) {
    ArrayList<User> users = new ArrayList<>();

    String eventUsers = Endpoints.EVENTUSERS.replace(":eventId", eventId);
    JSONArray jArray = Json.getJsonArray(
      eventUsers,
      new String[] {"userid"},
      new String[] {"amcolash"}
    );

    if (jArray != null) {
      for (int i = 0; i < jArray.length(); i++) {
        try {
          JSONObject oneObject = jArray.getJSONObject(i);
          // Pulling items from the array
          String name = oneObject.getString("name");
          String userId = oneObject.getString("userId");
          String role = oneObject.getString("role");
          users.add(new User(name, userId, role));
        } catch (Exception e) {
          e.printStackTrace();
          users = null;
          break;
        }
      }
    } else {
      users = null;
    }

    return users;
  }

}
