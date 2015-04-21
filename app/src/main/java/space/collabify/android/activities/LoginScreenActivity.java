package space.collabify.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import org.json.JSONObject;

import kaaes.spotify.webapi.android.SpotifyApi;
import space.collabify.android.*;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.collabify.api.CollabifyApi;
import space.collabify.android.managers.AppManager;
import space.collabify.android.models.User;

/**
 * This file was born on March 11 at 13:57
 */
public class LoginScreenActivity extends CollabifyActivity implements ConnectionStateCallback {
    private final String TAG = getClass().getSimpleName();

    private static final String CLIENT_ID = "a61533696d61400dac8c6dceb884a575";
    private static final String REDIRECT_URI = "collabify-space-login://callback";

    //request code that will be used to verify if the result comes from correct activity
    //can be any integer
    private static final int REQUEST_CODE = 1337;

    private ProgressDialog progress;
    private Context mainContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = false;

        //don't want any user or event information to persist after a user logs out
        mAppManager.clearData();
    }

    public void loginWithSpotify(View view) {
        // TODO: at this point don't know if the user will need streaming scope, will have to be done on choose mode
        //may want to have some sort of 'load screen' that just shows our icon and name for like two seconds
        // before prompting the user to sign up through spotify
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private", "user-library-read", "user-read-email", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            //checking for successful login response
            String error = response.getError();
            if(error != null) {
                //TODO: not sure yet what may cause this case
                // and show text or some shit?
                Toast.makeText(this, "login error occured", Toast.LENGTH_LONG).show();
                Log.e("AUTH", error);
                return;
            }

            if(response.getAccessToken() != null && response.getType().name().equalsIgnoreCase("TOKEN")) {
                //TODO: something with the response.getAccessToken() but not sure what yet
                AppManager.getInstance().getUser().setAccessToken(response.getAccessToken());
                SpotifyApi mSpotifyApi = new SpotifyApi();
                mSpotifyApi.setAccessToken(response.getAccessToken());
                AppManager.getInstance().setSpotifyApi(mSpotifyApi);


                new LongOperation().execute(response.getAccessToken());

                mainContext = this;
                progress = ProgressDialog.show(this, "Logging you in",  "Crunching the numbers", true);
            }
        }
    }

  private class LongOperation extends AsyncTask<String, Void, JSONObject> {

    @Override
    protected JSONObject doInBackground(String... params) {
      try {
        JSONObject me = Json.getJsonObject(
          "https://api.spotify.com/v1/me",
          new String[] {"Authorization"},
          new String[] {"Bearer " + params[0]}
        );

        JSONObject myUser = new JSONObject();
        myUser.put("name", me.getString("display_name"));

        Json.postJSONObject(
          Endpoints.USERS,
          myUser,
          new String[] {"userid"},
          new String[] {me.getString("id")}
        );

        return me;
      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(mainContext, "login error occured", Toast.LENGTH_LONG).show();
      }
      return null;
    }

    @Override
    protected void onPostExecute(JSONObject me) {
      progress.dismiss();

      try {
        finish();

        User u = mAppManager.getUser();
        u.setName(me.getString("display_name"));
        u.setPremium(me.getString("product").equals("premium"));
        u.setId(me.getString("id"));

        mAppManager.getCollabifyClient().getCollabifyApi().setCurrentUserId(me.getString("id"));

        Intent i = new Intent(mainContext, JoinEventActivity.class);
        startActivity(i);

      } catch (Exception e) {
        e.printStackTrace();
        Toast.makeText(mainContext, "login error occured", Toast.LENGTH_LONG).show();
      }

    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {}
  }


    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable throwable) {
        Log.d(TAG, "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d(TAG, "Received connection message: " + s);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
