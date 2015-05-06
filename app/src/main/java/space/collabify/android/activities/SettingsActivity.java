package space.collabify.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.managers.CollabifyResponseCallback;

public class SettingsActivity extends CollabifyActivity {
    private static CheckBox mShowUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mShowUsername = (CheckBox) findViewById(R.id.show_username_checkbox);
        if(mAppManager.getUser() != null) {
            if (mAppManager.getUser().getSettings().isShowName()) {
                Log.d(TAG, "checkbox:true");
                mShowUsername.setChecked(true);
            } else {
                mShowUsername.setChecked(false);
                Log.d(TAG, "checkbox:false");
            }
        }

        SHOW_SETTINGS = false;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = false;
    }

    public void toAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void showUsernameUpdate(View view){
        Log.d(TAG, "check box hit");
        if(mAppManager.getUser() != null) {
            if (mAppManager.getUser().getSettings().isShowName()) {
                mAppManager.getUser().getSettings().setShowName(false);
            } else {
                mAppManager.getUser().getSettings().setShowName(true);
            }
            mAppManager.updateUser(null);
        }
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
