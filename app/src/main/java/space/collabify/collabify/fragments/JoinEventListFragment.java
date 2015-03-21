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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.ListFragment;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Event tmpEvent = new Event("Waiting for server", false, null);
        List<Event> temp = new ArrayList<>();
        temp.add(tmpEvent);
        EventListAdapter adapter = new EventListAdapter(getActivity().getApplicationContext(), temp);
        setListAdapter(adapter);

        setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initiateRefresh();
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //super.onListItemClick(l, v, position, id);
        String item = (String)l.getItemAtPosition(position);

    }

    /**
     * Starts a LoadEventsTask in background with user location
     */
    private void initiateRefresh() {
        Log.i(TAG, "initiate event list refresh");
        LoadEventsRequest request = new LoadEventsRequest();
        request.userLocation = mLastUserLocation;
        new LoadEventsTask().execute(request);
    }

    /**
     * Callback from the LoadEventsTask for when the data has been fetched from server
     * @param events list of events close to user
     */
    private void onRefreshComplete(List<Event> events){
        // Remove all items from the ListAdapter, and then replace them with the new items
        EventListAdapter adapter = (EventListAdapter) getListAdapter();
        adapter.clear();
        for (Event event: events) {
            adapter.add(event);
        }

        // Stop the refreshing indicator
        setRefreshing(false);
    }

    /**
     * Set the latest user location for event queries
     * @param location user location
     */
    public void updateLocation(Location location) {
        mLastUserLocation = location;
    }

    /**
     * Displays the refresh circle and gets the events around the user
     */
    public void initializeList() {
        if(!isRefreshing()){
            setRefreshing(true);
            initiateRefresh();
        }
    }


    /**
     * Handles the display of events in a row
     */
    private class EventListAdapter extends ArrayAdapter<Event> {
        private EventListAdapter(Context context, List<Event> objects) {
            super(context, R.layout.join_event_list_row, objects);
        }

        private EventListAdapter(Context context, Event[] events) {
            super(context, R.layout.join_event_list_row,events);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView  = inflater.inflate(R.layout.join_event_list_row, parent, false);

            Event eventItem = getItem(position);
            TextView rowText = (TextView) customView.findViewById(R.id.join_event_row_text);
            ImageView rowImage = (ImageView) customView.findViewById(R.id.join_event_row_image);

            rowText.setText(eventItem.getName());
            int visibility = eventItem.isProtectedEvent()? View.VISIBLE: View.INVISIBLE;
            rowImage.setVisibility(visibility);

            return customView;
        }
    }

    /**
     * A background task to fetch events from our server without slowing ui
     */
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
