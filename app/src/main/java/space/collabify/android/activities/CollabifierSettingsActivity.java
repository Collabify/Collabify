package space.collabify.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;


public class CollabifierSettingsActivity extends CollabifyActivity { //BaseSettingsActivity

    private static CheckBox mShowUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collabifier_settings);

        mShowUsername = (CheckBox) findViewById(R.id.show_username_checkbox);
        if (mAppManager.getUser().getSettings().isShowName()){
            Log.d(TAG, "checkbox:true");
            mShowUsername.setChecked(true);
        }
        else{
            mShowUsername.setChecked(false);
            Log.d(TAG, "checkbox:false");
        }

        SHOW_SETTINGS = false;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = false;
    }

    public void showUsernameUpdate(View view){
        Log.d(TAG, "check box hit");
        if (mAppManager.getUser().getSettings().isShowName()){
            mAppManager.getUser().getSettings().setShowName(false);
        }
        else{
            mAppManager.getUser().getSettings().setShowName(true);
        }
        mAppManager.updateUser(null);
    }
    public void toAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void toLogOut(View view) {
        mAppManager.getUser().setRole("NoRole");
        mAppManager.logout(getApplicationContext(), null);
        //return to login activity
        Intent intent = new Intent(this, LoginScreenActivity.class);
        startActivity(intent);
        finish();
    }

}
