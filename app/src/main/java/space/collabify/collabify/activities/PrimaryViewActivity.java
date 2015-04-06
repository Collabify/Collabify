package space.collabify.collabify.activities;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import space.collabify.collabify.TabsPagerAdapter;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.fragments.PlaylistFragment;
import space.collabify.collabify.models.Playlist;
import space.collabify.collabify.models.Song;

/**
 * This file was born on March 11 at 14:01
 */
public class PrimaryViewActivity extends CollabifyActivity implements ActionBar.TabListener, PlaylistFragment.OnPlaylistUpdateRequestListener {
    protected ViewPager mViewPager;
    protected TabsPagerAdapter mAdapter;
    protected ActionBar mActionBar;

    @Override
    public void onPlaylistUpdateRequest() {
        Playlist currentPlaylist = mCollabifyClient.getEventPlaylist();
        final int PLAYLIST_POSITION = 1;
        Fragment currentFragment =  mAdapter.getRegisteredFragment(PLAYLIST_POSITION);
        if(currentFragment instanceof PlaylistFragment){
            PlaylistFragment playlistFragment = (PlaylistFragment) currentFragment;
            playlistFragment.updatePlaylist(currentPlaylist);
        }else {
            //can't update if not visible
        }
    }

    @Override
    public void onTabSelected (ActionBar.Tab tab, android.support.v4.app.FragmentTransaction
            fragmentTransaction){
        mViewPager.setCurrentItem(tab.getPosition());
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
