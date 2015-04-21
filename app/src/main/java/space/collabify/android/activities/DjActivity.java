package space.collabify.android.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.*;
import space.collabify.android.managers.CollabifyResponseCallback;

/**
 * This file was born on March 11 at 14:02
 */
public class DjActivity extends PrimaryViewActivity {
    private static final String TAG = DjActivity.class.getSimpleName();
    // Tab titles
    private String[] tabs = {"Player", "Playlist", "DJ Tracks", "User List"};
    private int[] icons = {R.drawable.ic_player, R.drawable.ic_playlist, R.drawable.ic_dj, R.drawable.ic_users};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = true;
        SHOW_LOGOUT = true;

        // Initilization
        mViewPager = (ViewPager) findViewById(R.id.djPager);
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

        mViewPager.setCurrentItem(1);

    }



    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // construct the dialog
        builder.setTitle(R.string.title_end_event);
        builder.setMessage(R.string.label_end_event);

        // exit if OK button pressed
        builder.setPositiveButton(getString(R.string.ok_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAppManager.endEvent(new CollabifyResponseCallback() {
                            @Override
                            public void exception(Exception e) {
                                Log.e(TAG, "Exception encountered while ending event.");
                            }

                            @Override
                            public void success(Response response) {
                                finish();
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Log.e(TAG, "Failed to end event: " + retrofitError.getMessage());
                            }
                        });
                    }
                }
        );

        // close dialog on Cancel button pressed
        builder.setNegativeButton(getString(R.string.cancel_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }
        );

        builder.show();
    }


}