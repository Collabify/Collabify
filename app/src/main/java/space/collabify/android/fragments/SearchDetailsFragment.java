package space.collabify.android.fragments;


import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import space.collabify.android.collabify.CollabifyClient;
import space.collabify.android.activities.DetailedSearchActivity;
import space.collabify.android.collabify.models.domain.Song;
import space.collabify.android.models.User;


/**
 * This file was born on March 11, at 15:53
 */

public class SearchDetailsFragment extends ListFragment {

    protected DetailedSearchActivity mParentActivity;
    protected SearchDetailsListAdapter mAdapter;

    private List<Song> songs;

    private CollabifyClient mClient = CollabifyClient.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create the list fragment's content view by calling the super method
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);


        return listFragmentView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mParentActivity = (DetailedSearchActivity)getActivity();

        //will probably just want empty list, but this is useful for debug
        List<Song> temp = new ArrayList<>();
        User user = mParentActivity.getCurrentUser();
        mAdapter = new SearchDetailsListAdapter(mParentActivity.getApplicationContext(), temp, mParentActivity.getCurrentUser(), this);
        setListAdapter(mAdapter);
    }


    public void populateSongList(final List<Song> songs){

        mAdapter.clear();

        for(Song song : songs) {
            mAdapter.add(song);
        }
    }


    public void setmParentActivity(DetailedSearchActivity mParentActivity){
        this.mParentActivity = mParentActivity;
    }

    public DetailedSearchActivity getmParentActivity(){
        return this.mParentActivity;
    }
}
