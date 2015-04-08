package space.collabify.collabify.requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import space.collabify.collabify.Endpoints;
import space.collabify.collabify.Json;
import space.collabify.collabify.models.User;

/**
 * This file was born on March 20, at 19:50
 * Includes whatever data is needed to request a list of events from the server
 */
public class UsersRequest {

  public ArrayList<User> get(int eventId) {
    ArrayList<User> users = new ArrayList<>();

    String eventUsers = Endpoints.USERS.replace(":eventId", String.valueOf(eventId));
    JSONArray jArray = Json.getJsonArray(eventUsers);

    if (jArray != null) {
      for (int i = 0; i < jArray.length(); i++) {
        try {
          JSONObject oneObject = jArray.getJSONObject(i);
          // Pulling items from the array
          String name = oneObject.getString("name");
          int userId = oneObject.getInt("userId");
          users.add(new User(name, userId));
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
