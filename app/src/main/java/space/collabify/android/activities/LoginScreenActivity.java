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
import space.collabify.android.managers.CollabifyResponseCallback;

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

            if(response.getAccessToken() != null && response.getType().name().equalsIgnoreCase("TOKEN")) {
                mainContext = this;
                progress = ProgressDialog.show(this, "Logging you in",  "Crunching the numbers", true);

                // handle all post login stuff in the app manager
                AppManager.getInstance().login(response.getAccessToken(), new CollabifyResponseCallback() {
                    @Override
                    public void success(Response response) {
                        progress.dismiss();
                        finish();
                        Intent i = new Intent(mainContext, JoinEventActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        progress.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clearAppData();
                                Toast.makeText(mainContext, "login error occured", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                    @Override
                    public void exception(Exception e) {
                        progress.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clearAppData();
                                Toast.makeText(mainContext, "login error occured", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        }
    }

    private void clearAppData(){
        // use old hacky way, which can be removed
        // once minSdkVersion goes above 19 in a few years.
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
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
