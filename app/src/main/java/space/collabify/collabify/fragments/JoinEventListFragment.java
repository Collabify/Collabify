package space.collabify.collabify.fragments;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.ListFragment;

import com.google.android.gms.location.LocationListener;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.LoadEventsRequest;
import space.collabify.collabify.R;
import space.collabify.collabify.ServerManager;
import space.collabify.collabify.models.Event;

/**
 * Created by andrew on 3/20/15.
 */
public class JoinEventListFragment extends SwipeRefreshListFragment {
    private static final String TAG = JoinEventListFragment.class.getSimpleName();

    private Location mLastUserLocation;


    public View inflateFragment(int i, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return null;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<String> temp = new ArrayList<>();
        temp.add("empty");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                temp);
        setListAdapter(adapter);

        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
    }

    private void initiateRefresh() {
        Log.i(TAG, "initiate event list refresh");
        LoadEventsRequest request = new LoadEventsRequest();
        request.userLocation = mLastUserLocation;
        new LoadEventsTask().execute(request);
    }

    private void onRefreshComplete(List<Event> events){
        // Remove all items from the ListAdapter, and then replace them with the new items
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();
        adapter.clear();
        for (Event event: events) {
            adapter.add(event.getName());
        }

        // Stop the refreshing indicator
        setRefreshing(false);
    }

    public void updateLocation(Location location) {
        mLastUserLocation = location;
    }


    private class LoadEventsTask extends AsyncTask<LoadEventsRequest, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(LoadEventsRequest... params) {
            return ServerManager.getInstance().getEvents(params[0]);
        }

        @Override
        protected void onPostExecute(List<Event> events) {
            super.onPostExecute(events);

            //post results
            onRefreshComplete(events);
        }
    }
}
