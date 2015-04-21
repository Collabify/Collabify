package space.collabify.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.managers.CollabifyResponseCallback;


public class DjSettingsActivity extends CollabifyActivity { //BaseSettingsActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_settings);

        SHOW_SETTINGS = false;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = false;
    }

    public void toAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void toLogOut(View view) {

        final Intent intent = new Intent(this, LoginScreenActivity.class);

        mAppManager.logout(getApplicationContext(), new CollabifyResponseCallback() {
            @Override
            public void exception(Exception e) {

            }

            @Override
            public void success(Response response) {
                //return to login activity
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

}
