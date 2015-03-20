package space.collabify.collabify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.playback.ConnectionStateCallback;

import space.collabify.collabify.*;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
    }

    public void loginWithSpotify(View view) {
        // TODO: at this point don't know if the user will need streaming scope, will have to be done on choose mode
        //may want to have some sort of 'load screen' that just shows our icon and name for like two seconds
        // before prompting the user to sign up through spotify
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private", "user-library-read", "user-read-private"});
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
                return;
            }

            if(response.getAccessToken() != null && response.getType().name().equalsIgnoreCase("TOKEN")) {
                //TODO: something with the response.accessToken() but not sure what yet
                Intent i = new Intent(this, ModeSelectActivity.class);
                startActivity(i);
            }
        }
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
