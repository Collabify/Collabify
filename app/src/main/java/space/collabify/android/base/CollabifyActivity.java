package space.collabify.android.base;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.*;
import space.collabify.android.activities.CollabifierSettingsActivity;
import space.collabify.android.activities.DjSettingsActivity;
import space.collabify.android.activities.LoginScreenActivity;
import space.collabify.android.activities.SettingsActivity;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyResponseCallback;
import space.collabify.android.models.User;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * This file was born on March 11 at 13:44
 */
public class CollabifyActivity extends ActionBarActivity  {
    private static final String TAG = CollabifyActivity.class.getSimpleName();
    protected AppManager mAppManager;
    protected User mUser;
    protected String mRole;

    protected boolean SHOW_SETTINGS = false;
    protected boolean SHOW_LEAVE = false;
    protected boolean SHOW_LOGOUT = false;
    //protected CollabifyActivity mParentActivity;


    public CollabifyActivity(){
        this.mAppManager = AppManager.getInstance();
        //mParentActivity = (CollabifyActivity) getActivity();
        this.mUser = getCurrentUser();

        invalidateOptionsMenu();
    }

    public CollabifyActivity(AppManager mAppManager) {
        this.mAppManager = mAppManager;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collabify_actions, menu);

        menu.findItem(R.id.action_settings).setVisible(SHOW_SETTINGS);
        menu.findItem(R.id.action_leave).setVisible(SHOW_LEAVE);
        menu.findItem(R.id.action_logout).setVisible(SHOW_LOGOUT);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle presses on the action bar items
        switch (item.getItemId()){
            case R.id.action_settings:
                //TODO: the role is null before entering a mode. The role is also not being reset
                // after leaving DJ or Collabifier mode
                if (mUser.getRole().isNoRole()) {
                    /** default settings */
                    openSettings();
                    return true;
                }
                else if (mUser.getRole().isDJ()) {
                    openDJSettings();
                    return true;
                }
                else if (mUser.getRole().isCollabifier()) {
                    openCollabifierSettings();
                    return true;
                }
                else if (mUser.getRole().isBlacklisted()) {
                    //openBlacklistedSettings();
                    return true;
                }
                else if (mUser.getRole().isPromoted()) {
                    //openPromotedSettings();
                    return true;
                }
                else {
                    /** default settings */
                    openSettings();
                    return true;
                }

            /*
            //case for Collabifier settings
            case R.id.action_Collabifier_settings:
                openCollabifierSettings();
                return true;

            //case for DJ settings
            case R.id.action_DJ_settings:
                openDJSettings();
                return true;
            */

            case R.id.action_logout:
                final Context ctx = this;
                mAppManager.logout(this, new CollabifyResponseCallback() {
                    @Override
                    public void success(Response response) {
                        //return to login activity
                        Intent intent = new Intent(ctx, LoginScreenActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                    }

                    @Override
                    public void exception(Exception e) {

                    }
                });

                return true;

          case R.id.action_leave:
            Intent leave = new Intent("leave_event");
            sendBroadcast(leave);
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

    // @todo have only 1 settings activity, check user mode to display correct settings
    /** Launch Collabifier settings activity */
    private void openCollabifierSettings() {
        //don't want to go to settings if we are already there
        Intent intent = new Intent(getApplicationContext(), CollabifierSettingsActivity.class);
        startActivity(intent);

    }

    /** Launch DJ settings activity */
    private void openDJSettings() {
        //don't want to go to settings if we are already there
        Intent intent = new Intent(getApplicationContext(), DjSettingsActivity.class);
        startActivity(intent);
    }


    /**
     * Gets the current user from the appmanager....
     * Not sure if Appmanager should be public, or if it is
     * better to expose parts of it in this way...
     * @return this User, or null if not possible
     */
    public User getCurrentUser(){
        if(mAppManager == null){
            return null;
        }
        return mAppManager.getUser();
    }

}
