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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.spotify.sdk.android.player.Player;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.*;
import space.collabify.android.fragments.BasePlayerFragment;
import space.collabify.android.managers.CollabifyResponseCallback;

/**
 * This file was born on March 11 at 14:02
 */
public class DjActivity extends PrimaryViewActivity implements
        BasePlayerFragment.PlayerFragmentListener, PlayerHandler.PlayerHandlerListener{
    private static final String TAG = DjActivity.class.getSimpleName();
    // Tab titles
    private String[] tabs = {"Player", "Playlist", "DJ Tracks", "User List"};
    private int[] icons = {R.drawable.ic_player, R.drawable.ic_playlist, R.drawable.ic_dj, R.drawable.ic_users};

    private PlayerHandler mPlayerHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        SHOW_SETTINGS = true;
        SHOW_END = true;
        SHOW_LOGOUT = true;

        mPlayerHandler = new PlayerHandler(this, this);


        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setLogo(R.drawable.ic_dj);
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

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
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this, mAppManager.getUser().getRole());

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
    protected void onDestroy() {
        mPlayerHandler.onDestroy();
        super.onDestroy();
    }

    @Override
    public PlayerHandler getPlayerHandler() {
        return mPlayerHandler;
    }

    @Override
    public void onBackPressed() {
        endEvent();
    }

    @Override
    public void startNextSong() {
        BasePlayerFragment playerFragment = (BasePlayerFragment) mAdapter.getRegisteredFragment(0);

        if(playerFragment == null){
            Log.w(TAG, "Couldn't access player fragment to start next song");
            return;
        }
        playerFragment.updatePlayerView();

    }
    @Override
    protected void onResume() {
        super.onResume();
        String eventName = mAppManager.getEvent().getName();
        setTitle(eventName);
    }

}