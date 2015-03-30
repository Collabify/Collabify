package space.collabify.collabify.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost;

import space.collabify.collabify.*;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.fragments.CollabifierPlaylistFragment;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;

/**
 * This file was born on March 11 at 14:02
 */
public class CollabifierActivity extends CollabifyActivity implements ActionBar.TabListener, CollabifierPlaylistFragment.OnPlaylistUpdateRequestListener{
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;

    private CollabifierPlaylistFragment mPlaylistFragment;

    // Tab titles
    private String[] tabs = {"Player", "Playlist", "DJ Tracks"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collabifier);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.collabifierPager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


        actionBar = getSupportActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), mAppManager.getUser().getRole());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

    }

    @Override
    public void onBackPressed () {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // construct the dialog
        builder.setTitle(R.string.title_exit_event);
        builder.setMessage(R.string.label_exit_event);

        // exit if OK button pressed
        builder.setPositiveButton(getString(R.string.ok_button),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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

    @Override
    public void onTabSelected (ActionBar.Tab tab, android.support.v4.app.FragmentTransaction
    fragmentTransaction){
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected (ActionBar.Tab tab, android.support.v4.app.FragmentTransaction
    fragmentTransaction){
    }

    @Override
    public void onTabReselected (ActionBar.Tab tab, android.support.v4.app.FragmentTransaction
    fragmentTransaction){
    }

    @Override
    public void onPlaylistUpdateRequest() {
        Playlist currentPlaylist = mCollabifyClient.getEventPlaylist();
        final int PLAYLIST_POSITION = 1;
        Fragment currentFragment =  mAdapter.getRegisteredFragment(PLAYLIST_POSITION);
        if(currentFragment instanceof CollabifierPlaylistFragment){
            CollabifierPlaylistFragment playlistFragment = (CollabifierPlaylistFragment) currentFragment;
            playlistFragment.updatePlaylist(currentPlaylist);
        }else {
            //can't update if not visible
        }
    }

    @Override
    public Song getSongFromId(String songId) {
        //assumes that all songs are uniquely identified by their id
        return mCollabifyClient.getSongById(songId);
    }

    @Override
    public void upvoteSong(Song song) {
        mCollabifyClient.upvoteSong(mAppManager.getUser(), song);
    }

    @Override
    public void downvoteSong(Song song) {
        mCollabifyClient.downvoteSong(mAppManager.getUser(), song);
    }

    @Override
    public void deleteSong(Song song) {
        mCollabifyClient.deleteSong(mAppManager.getUser(), song);
    }

    @Override
    public void clearSongVote(Song song) {
        mCollabifyClient.clearSongVote(mAppManager.getUser(), song);
    }
}
