package space.collabify.collabify.base;

import space.collabify.collabify.*;
import space.collabify.collabify.activities.LoginScreenActivity;
import space.collabify.collabify.activities.SettingsActivity;
import space.collabify.collabify.managers.AppManager;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * This file was born on March 11 at 13:44
 */
public class CollabifyActivity extends ActionBarActivity {
    protected AppManager mAppManager;
    protected CollabifyClient collabifyClient;

    public CollabifyActivity(){
        this.mAppManager = AppManager.getInstance();
        this.collabifyClient = collabifyClient.getInstance();
    }

    public CollabifyActivity(AppManager mAppManager, CollabifyClient collabifyClient) {
        this.mAppManager = mAppManager;
        this.collabifyClient = collabifyClient;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collabify_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle presses on the action bar items
        switch (item.getItemId()){
            case R.id.action_settings:
                openSettings();
                return true;

            case R.id.action_logout:
                mAppManager.spotifyLogout(getApplicationContext());
                //return to login activity
                Intent intent = new Intent(this, LoginScreenActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /** Launch settings activity */
    private void openSettings() {
        //don't want to go to settings if we are already there
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);

    }
}
