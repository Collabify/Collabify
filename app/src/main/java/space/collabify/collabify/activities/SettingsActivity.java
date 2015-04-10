package space.collabify.collabify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.models.User;

public class SettingsActivity extends CollabifyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                //do nothing, avoid multiple of same activity
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toLogOut(View view) {
        mAppManager.getUser().setRole("NoRole");
        mAppManager.spotifyLogout(getApplicationContext());
        //return to login activity
        Intent intent = new Intent(this, LoginScreenActivity.class);
        startActivity(intent);
    }

}
