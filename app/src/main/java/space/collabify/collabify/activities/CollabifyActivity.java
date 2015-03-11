package space.collabify.collabify;

import android.support.v7.app.ActionBarActivity;

/**
 * This file was born on March 11 at 13:44
 */
public class CollabifyActivity extends ActionBarActivity {
    protected AppManager mAppManager;
    protected ServerManager mServerManager;

    public CollabifyActivity(){

    }

    public CollabifyActivity(AppManager mAppManager, ServerManager mServerManager) {
        this.mAppManager = mAppManager;
        this.mServerManager = mServerManager;
    }
}
