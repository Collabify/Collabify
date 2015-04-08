package space.collabify.collabify;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import space.collabify.collabify.models.Playlist;

/**
 * Helper class to deal with Java's painful implementation of JSON
 * Created by andrew on 4/1/15.
 */
public class Json {

  /**
   * Get JSONObject from given uri, wraps helper functions together
   * @param uri URI of the given JSON resource
   * @return A JSONObject
   */
  public static JSONObject getJsonObject(String uri) {
    String content = getJSONString(uri);
    return toJSON(content);
  }

  /**
   * Get JSONArray from given uri, wraps helper functions together
   * @param key JSON array key
   * @param uri URI of the given JSON resource
   * @return A JSONArray
   */
  public static JSONArray getJsonArray(String key, String uri) {
    String content = getJSONString(uri);
    JSONObject o = toJSON(content);
    return toJSONArray(key, o);
  }

  /**
   * Get JSONArray from given uri, wraps helper functions together
   * @param uri URI of the given JSON resource
   * @return A JSONArray
   */
  public static JSONArray getJsonArray(String uri) {
    String content = getJSONString(uri);
    return toJSONArray(content);
  }

  /**
   * Return the JSONArray of a given JSON object and key
   * @param key JSON array key
   * @return JSONArray from the given string
   */
  public static JSONArray toJSONArray(String key, JSONObject object) {
    try {
      return object.getJSONArray(key);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * Return the JSONArray of a given JSON string
   * @param content JSON string
   * @return JSONArray from the given string
   */
  public static JSONArray toJSONArray(String content) {
    try {
      return new JSONArray(content);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Return the JSONObject of a given JSON string
   * @param content JSON string
   * @return JSONObject from the given string
   */
  private static JSONObject toJSON(String content) {
    try {
      JSONObject json = new JSONObject(content);
      return json;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Overloaded version of original getJSONString (Doesn't use headers)
   * Returns the string version of json for a given uri
   *
   * @param uri         URI to get the JSON from
   * @return A string version of the JSON
   * */
  private static String getJSONString(String uri) {
    return getJSONString(uri, null, null);
  }

  /**
   * Returns the string version of json for a given uri
   * Lovely code taken from: http://www.vogella.com/tutorials/AndroidJSON/article.html
   *
   * @param uri         URI to get the JSON from
   * @param headerKey   Array of header keys to send
   * @param headerValue Array of header values to send
   * @return A string version of the JSON
   * */
  private static String getJSONString(String uri, String[] headerKey, String[] headerValue) {
    StringBuilder builder = new StringBuilder();
    HttpClient client = new DefaultHttpClient();
    HttpGet httpGet = new HttpGet(uri);

    if (headerKey != null && headerValue != null && headerKey.length == headerValue.length) {
      for (int i = 0; i < headerKey.length; i++) {
        httpGet.addHeader(headerKey[i], headerValue[i]);
      }
    }
    try {
      HttpResponse response = client.execute(httpGet);
      StatusLine statusLine = response.getStatusLine();
      int statusCode = statusLine.getStatusCode();
      if (statusCode == 200) {
        HttpEntity entity = response.getEntity();
        InputStream content = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        String line;
        while ((line = reader.readLine()) != null) {
          builder.append(line);
        }
      } else {
        Log.e("JSON", "Failed to download file");
      }
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return builder.toString();
  }

}
