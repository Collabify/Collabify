package space.collabify.collabify.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import space.collabify.collabify.R;
import space.collabify.collabify.TabsPagerAdapter;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.fragments.PlaylistFragment;
import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;
import space.collabify.collabify.requests.VoteRequest;

/**
 * This file was born on March 11 at 14:01
 */
public class PrimaryViewActivity extends CollabifyActivity implements ActionBar.TabListener, PlaylistFragment.OnPlaylistUpdateRequestListener {
    protected ViewPager mViewPager;
    protected TabsPagerAdapter mAdapter;
    protected ActionBar mActionBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.menu_song_search, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused){
                if(!queryTextFocused){
                    searchItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){

                SearchView mySearchView = (SearchView) findViewById(R.id.action_search);

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(mySearchView.getWindowToken(), 0);

                return handleQuery(query);
            }

            @Override
            public boolean onQueryTextChange(String query){
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * starts detailed search activity
     *
     * @param query
     */
    public boolean handleQuery(String query) {

        Intent intent = new Intent(PrimaryViewActivity.this, DetailedSearchActivity.class);

        intent.putExtra("query", query);

        startActivity(intent);

        return true;
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
        new VoteTask().execute(new VoteRequest(song, VoteRequest.VoteType.UPVOTE));
    }

    @Override
    public void downvoteSong(Song song) {
        new VoteTask().execute(new VoteRequest(song, VoteRequest.VoteType.DOWNVOTE));
    }

    @Override
    public void deleteSong(Song song) {
        new DeleteSongTask().execute(song);
    }

    @Override
    public void clearSongVote(Song song) {
        new VoteTask().execute(new VoteRequest(song, VoteRequest.VoteType.CLEAR_VOTE));
    }

    private class VoteTask extends AsyncTask<VoteRequest, Void, Void> {

        @Override
        protected Void doInBackground(VoteRequest... params) {
            VoteRequest request = params[0];
            User user = mAppManager.getUser();
            switch (request.voteType){
                case UPVOTE:
                    mCollabifyClient.upvoteSong(user, request.song);
                    break;

                case DOWNVOTE:
                    mCollabifyClient.downvoteSong(user, request.song);
                    break;

                case CLEAR_VOTE:
                    mCollabifyClient.clearSongVote(user, request.song);
                    break;

                default:
                    //do nothing
                    break;
            }

            return null;
        }
    }

    private class DeleteSongTask extends AsyncTask<Song, Void, Void> {

        @Override
        protected Void doInBackground(Song... params) {
            mCollabifyClient.deleteSong(mAppManager.getUser(), params[0]);
            return null;
        }

    }
}
