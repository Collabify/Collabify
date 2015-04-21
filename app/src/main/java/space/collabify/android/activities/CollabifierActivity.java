package space.collabify.android.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.*;
import space.collabify.android.collabify.models.domain.User;
import space.collabify.android.managers.CollabifyResponseCallback;

/**
 * This file was born on March 11 at 14:02
 */

public class CollabifierActivity extends PrimaryViewActivity {
    // Tab titles
    private static final String TAG = JoinEventActivity.class.getSimpleName();

    private String[] tabs = {"Player", "Playlist", "DJ Tracks"};
    private int[] icons = {R.drawable.ic_player, R.drawable.ic_playlist, R.drawable.ic_dj};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collabifier);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = true;
        SHOW_LOGOUT = true;

        // Initilization
        mViewPager = (ViewPager) findViewById(R.id.collabifierPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                mActionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        mActionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), mAppManager.getUser().getRole());

        mViewPager.setAdapter(mAdapter);
        mActionBar.setHomeButtonEnabled(false);
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (int i = 0; i < tabs.length; i++) {

            View tabView = getLayoutInflater().inflate(R.layout.tab_layout, null);
            TextView tabText = (TextView) tabView.findViewById(R.id.tabText);
            tabText.setText(tabs[i]);

            ImageView tabImage = (ImageView) tabView.findViewById(R.id.tabIcon);
            tabImage.setImageDrawable(getResources().getDrawable(icons[i]));

            mActionBar.addTab(mActionBar.newTab()
                    .setCustomView(tabView)
                    .setTabListener(this));
        }

        BroadcastReceiver broadcast_receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("leave_event")) {
                    leaveEvent();
                }
            }
        };
        registerReceiver(broadcast_receiver, new IntentFilter("leave_event"));

    }


    @Override
    public void onBackPressed () {
        leaveEvent();
    }

    public void leaveEvent() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // construct the dialog
        builder.setTitle(R.string.title_exit_event);
        builder.setMessage(R.string.label_exit_event);

        // exit if OK button pressed
        builder.setPositiveButton(getString(R.string.ok_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAppManager.leaveEvent(new CollabifyResponseCallback() {
                    @Override
                    public void success(Response response) {
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e(TAG, "Failed to leave event:\n" + error.toString());
                        finish();
                    }

                    @Override
                    public void exception(Exception e) {
                        Log.e(TAG, "Failed to leave event ");
                        finish();
                    }
                });
            }
        });

        // close dialog on Cancel button pressed
        builder.setNegativeButton(getString(R.string.cancel_button), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
