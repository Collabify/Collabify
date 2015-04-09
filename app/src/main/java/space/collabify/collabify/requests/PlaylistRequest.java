package space.collabify.collabify.requests;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import space.collabify.collabify.Endpoints;
import space.collabify.collabify.Json;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;

/**
 * This file was born on March 20, at 19:50
 * Includes whatever data is needed to request a list of events from the server
 */
public class PlaylistRequest {

  public Playlist get(String eventId) {
    ArrayList<Song> tempList = new ArrayList<>();

    // Get json data
    String eventPlaylist = Endpoints.PLAYLIST.replace(":eventId", String.valueOf(eventId));
    JSONArray jArray = Json.getJsonArray(
      eventPlaylist,
      new String[] {"userid"},
      new String[] {"user"}
    );

    if (jArray != null) {
      for (int i = 0; i < jArray.length(); i++) {
        try {
          JSONObject oneObject = jArray.getJSONObject(i);
          // Pulling items from the array
          String title = oneObject.getString("title");
          String artist = oneObject.getString("artist");
          String album = oneObject.getString("album");
          int year = oneObject.getInt("year");
          String id = oneObject.getString("songId");
          String albumArtwork = oneObject.getString("artworkUrl");
          String userId = oneObject.getString("userId");

          tempList.add(new Song(title, artist, album, year, id, albumArtwork, userId));
        } catch (Exception e) {
          tempList.add(new Song("Whoops, something went wrong!", "", "", 0, "", "", ""));
          tempList.add(new Song("Please pull to refresh", "", "", 0, "", "", ""));
          break;
        }
      }
    }

    return new Playlist("Cool new playlist", 0, tempList);
  }
}
