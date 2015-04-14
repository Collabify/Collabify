package space.collabify.android.activities;

import android.os.Bundle;

import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;

/**
 * Created by mr_rupert on 4/9/15.
 */
public class AboutActivity extends CollabifyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        SHOW_SETTINGS = false;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = false;
    }



}
