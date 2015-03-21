package space.collabify.collabify.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.ListFragment;
import android.widget.EditText;
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
import space.collabify.collabify.activities.JoinEventActivity;
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
        final Event item = (Event)l.getItemAtPosition(position);

        //either prompt to join event, or to enter password
        if(item == null){
            //not sure if possible, but can't do anything
            return;
        }

        if(item.isProtectedEvent()){
            setupPasswordDialog(item);
        }else {
            setupJoinDialog(item);
        }

    }

    /**
     * Prompts user if they want to actually join the event they clicked on
     * see helpful link: http://stackoverflow.com/questions/10903754/input-text-dialog-android
     * @param event
     */
    private void setupJoinDialog(final Event event){
        //just prompt to join
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.join_event_dialog_title));
        builder.setMessage(event.getName());
        builder.setPositiveButton(getString(R.string.join_event_dialog_positive_text),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //go on to event
                ((JoinEventActivity) getActivity()).toCollabifier(event);
            }
        });
        builder.setNegativeButton(getString(R.string.join_event_dialog_negative_text),
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    /**
     * Prompts user for event password and verifies it before going on
     * @param event
     */
    private void setupPasswordDialog(final Event event){
        //TODO: may have to change how password is handled/displayed
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Join Password Protected Event?");
        builder.setMessage(event.getName() + " - password:");

        //set up password input
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);
        builder.setPositiveButton(getString(R.string.join_event_dialog_positive_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //maybe go on to event
                        ((JoinEventActivity) getActivity()).toCollabifier(event, input.getText().toString());
                    }
                });
        builder.setNegativeButton(getString(R.string.join_event_dialog_negative_text),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.show();
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
