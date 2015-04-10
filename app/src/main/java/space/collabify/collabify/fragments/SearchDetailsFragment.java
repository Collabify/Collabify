package space.collabify.collabify.fragments;


import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.CollabifyClient;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.models.Song;
import space.collabify.collabify.models.User;


/**
 * This file was born on March 11, at 15:53
 */

public class SearchDetailsFragment extends ListFragment {

    protected CollabifyActivity mParentActivity;
    protected SearchDetailsListAdapter mAdapter;

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

        mParentActivity = (CollabifyActivity) getActivity();

        //will probably just want empty list, but this is useful for debug
        List<Song> temp = new ArrayList<>();
        temp.add(new Song("temp song", "temp artist", "temp album", -1, "temp id", "no artwork", ""));
        User user = mParentActivity.getCurrentUser();
        mAdapter = new SearchDetailsListAdapter(mParentActivity.getApplicationContext(), temp, mParentActivity.getCurrentUser(), this);
        setListAdapter(mAdapter);
    }


}
