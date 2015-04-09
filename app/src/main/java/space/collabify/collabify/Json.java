package space.collabify.collabify;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
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

  /***********************************************************************************************/
  /* GET */
  /***********************************************************************************************/

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
   * Get JSONObject from given uri, wraps helper functions together, includes headers
   * @param uri URI of the given JSON resource
   * @param headerKey   Array of header keys to send
   * @param headerValue Array of header values to send
   * @return A JSONObject
   */
  public static JSONObject getJsonObject(String uri, String[] headerKey, String[] headerValue) {
    String content = getJSONString(uri, headerKey, headerValue);
    return toJSON(content);
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
   * Get JSONArray from given uri, wraps helper functions together, includes headers
   * @param uri URI of the given JSON resource
   * @param headerKey   Array of header keys to send
   * @param headerValue Array of header values to send
   * @return A JSONArray
   */
  public static JSONArray getJsonArray(String uri, String[] headerKey, String[] headerValue) {
    String content = getJSONString(uri, headerKey, headerValue);
    return toJSONArray(content);
  }

  /**
   * Return the JSONArray of a given JSON string
   * @param content JSON string
   * @return JSONArray from the given string
   */
  private static JSONArray toJSONArray(String content) {
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
      return new JSONObject(content);
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

  /***********************************************************************************************/
  /* POST */
  /***********************************************************************************************/

  /**
   * Wrapper for postContent, posting a JSONObject, no extra headers
   * @param uri URI to send JSON to
   * @param j   JSONObject to send
   * @return    String response of post
   */
  public static String postJSONObject(String uri, JSONObject j) {
    return postContent(uri, j.toString(), null, null);
  }

  /**
   * Wrapper for postContent, posting a JSONObject, with extra headers
   * @param uri         URI to send JSON to
   * @param j           JSONObject to send
   * @param headerKey   Array of header keys to send
   * @param headerValue Array of header values to send
   * @return            String response of post
   */
  public static String postJSONObject(String uri, JSONObject j, String[] headerKey, String[] headerValue) {
    return postContent(uri, j.toString(), headerKey, headerValue);
  }

  /**
   * Wrapper for postContent, posting a JSONArray, no extra headers
   * @param uri URI to send JSON to
   * @param j   JSONArray to send
   * @return    String response of post
   */
  public static String postJSONArray(String uri, JSONArray j) {
    return postContent(uri, j.toString(), null, null);
  }

  /**
   * Wrapper for postContent, posting a JSONArray, with extra headers
   * @param uri         URI to send JSON to
   * @param j           JSONArray to send
   * @param headerKey   Array of header keys to send
   * @param headerValue Array of header values to send
   * @return            String response of post
   */
  public static String postJSONArray(String uri, JSONArray j, String[] headerKey, String[] headerValue) {
    return postContent(uri, j.toString(), headerKey, headerValue);
  }

  /**
   * Post a JSON object to a server, uses code from http://hmkcode.com/android-send-json-data-to-server/
   *
   * @param uri       URI to send JSON to
   * @param content   String to post
   * @return
   */
  private static String postContent(String uri, String content, String[] headerKey, String[] headerValue) {
    InputStream inputStream;
    String result = "";
    try {
      HttpClient httpclient = new DefaultHttpClient();
      HttpPost httpPost = new HttpPost(uri);

      StringEntity se = new StringEntity(content);
      httpPost.setEntity(se);

      httpPost.setHeader("Accept", "application/json");
      httpPost.setHeader("Content-type", "application/json");

      if (headerKey != null && headerValue != null && headerKey.length == headerValue.length) {
        for (int i = 0; i < headerKey.length; i++) {
          httpPost.addHeader(headerKey[i], headerValue[i]);
        }
      }

      HttpResponse httpResponse = httpclient.execute(httpPost);
      inputStream = httpResponse.getEntity().getContent();

      if(inputStream != null)
        result = convertInputStreamToString(inputStream);
      else
        result = "Did not work!";

    } catch (Exception e) {
      Log.d("InputStream", e.getLocalizedMessage());
    }

    return result;
  }

  /***********************************************************************************************/
  /* PUT */
  /***********************************************************************************************/

  /**
   * Wrapper for putContent, putting a JSONObject, no extra headers
   * @param uri URI to send JSON to
   * @param j   JSONObject to send
   * @return    String response of post
   */
  public static String putJSONObject(String uri, JSONObject j) {
    return putContent(uri, j.toString(), null, null);
  }

  /**
   * Wrapper for putContent, putting a JSONObject, with extra headers
   * @param uri         URI to send JSON to
   * @param j           JSONObject to send
   * @param headerKey   Array of header keys to send
   * @param headerValue Array of header values to send
   * @return            String response of post
   */
  public static String putJSONObject(String uri, JSONObject j, String[] headerKey, String[] headerValue) {
    return putContent(uri, j.toString(), headerKey, headerValue);
  }

  /**
   *Wrapper for putContent, putting a JSONArray, no extra headers
   * @param uri URI to send JSON to
   * @param j   JSONArray to send
   * @return    String response of post
   */
  public static String putJSONArray(String uri, JSONArray j) {
    return putContent(uri, j.toString(), null, null);
  }

  /**
   * Wrapper for putContent, putting a JSONArray, with extra headers
   * @param uri         URI to send JSON to
   * @param j           JSONArray to send
   * @param headerKey   Array of header keys to send
   * @param headerValue Array of header values to send
   * @return            String response of post
   */
  public static String putJSONArray(String uri, JSONArray j, String[] headerKey, String[] headerValue) {
    return putContent(uri, j.toString(), headerKey, headerValue);
  }

  /**
   * Put a JSON object to a server, modified from code at:
   * http://hmkcode.com/android-send-json-data-to-server/
   *
   * @param uri       URI to send JSON to
   * @param content   String to put
   * @return
   */
  private static String putContent(String uri, String content, String[] headerKey, String[] headerValue) {
    InputStream inputStream;
    String result = "";
    try {
      HttpClient httpclient = new DefaultHttpClient();
      HttpPut httpPut = new HttpPut(uri);

      StringEntity se = new StringEntity(content);
      httpPut.setEntity(se);

      httpPut.setHeader("Accept", "application/json");
      httpPut.setHeader("Content-type", "application/json");

      if (headerKey != null && headerValue != null && headerKey.length == headerValue.length) {
        for (int i = 0; i < headerKey.length; i++) {
          httpPut.addHeader(headerKey[i], headerValue[i]);
        }
      }

      HttpResponse httpResponse = httpclient.execute(httpPut);
      inputStream = httpResponse.getEntity().getContent();

      if(inputStream != null)
        result = convertInputStreamToString(inputStream);
      else
        result = "Did not work!";

    } catch (Exception e) {
      Log.d("InputStream", e.getLocalizedMessage());
    }

    return result;
  }

  /***********************************************************************************************/
  /* MISC */
  /***********************************************************************************************/

  /**
   * Method to convert an inputStream to a string. Found at:
   * from http://hmkcode.com/android-send-json-data-to-server/
   *
   * @param inputStream inputStream to convert
   * @return A string representation
   * @throws IOException
   */
  private static String convertInputStreamToString(InputStream inputStream) throws IOException{
    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
    String line = "";
    String result = "";
    while((line = bufferedReader.readLine()) != null)
      result += line;

    inputStream.close();
    return result;
  }

}
