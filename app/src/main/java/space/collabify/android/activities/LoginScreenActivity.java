package space.collabify.android.activities;

import android.app.ActivityManager;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;

import java.io.File;

import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.managers.CollabifyResponseCallback;
import space.collabify.android.models.Event;

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
    }

    public void loginWithSpotify(View view) {
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

            switch (response.getType()) {
                case TOKEN:

                    if (response.getAccessToken() != null) {
                        mainContext = this;
                        progress = ProgressDialog.show(this, "Logging you in",  "Crunching the numbers", true);

                        // handle all post login stuff in the app manager
                        AppManager.getInstance().login(response.getAccessToken(), new CollabifyCallback<String>() {
                            @Override
                            public void success(String eventId, Response response) {

                                Intent i = null;
                                if(eventId != null){
                                    //needed in order to get the event name, but can't because incorrectly sets user role to COLLABIFIER
                                    //mAppManager.joinEvent(eventId, null);
                                }
                                // the user is already at an event
                                if (!mAppManager.getUser().getRole().isNoRole() && eventId != null) {

                                    // the user is aj
                                    if (mAppManager.getUser().getRole().isDJ()) {
                                        i = new Intent(mainContext, DjActivity.class);
                                    }
                                    // the user is a collabifier
                                    else {
                                        i = new Intent(mainContext, CollabifierActivity.class);
                                    }
                                }
                                // the user will need to join an event
                                else {
                                    i = new Intent(mainContext, JoinEventActivity.class);
                                }



                                progress.dismiss();
                                finish();
                                startActivity(i);
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                        clearAppData();
                                        Toast.makeText(mainContext, "login error occured", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                            @Override
                            public void exception(Exception e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                        clearAppData();
                                        Toast.makeText(mainContext, "login error occured", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });

                    }

                    break;
                case ERROR:
                    Log.e(TAG, "an error occured attempting spotify login");
                    break;
                default:
                    // cancelled?
                    Log.i(TAG, "auth was possibly cancelled");
            }
        }
    }

    private void clearAppData(){
        //makes it so the login button doesn't automatically retry with same user/pass
        AuthenticationClient.logout(this);
    }

    @Override
    public void onBackPressed() {
        //TODO: better way?
        super.onBackPressed();
        finish();
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
