package space.collabify.collabify.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.CollabifyClient;
import space.collabify.collabify.LoadEventsRequest;
import space.collabify.collabify.LoadUsersRequest;
import space.collabify.collabify.R;
import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.User;

/**
 * This file was born on March 11, at 15:52
 */
public class UserListFragment extends SwipeRefreshListFragment {
  private static final String TAG = UserListFragment.class.getSimpleName();

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    User tmpUser = new User("Waiting for server", 999);
    List<User> temp = new ArrayList<>();
    temp.add(tmpUser);
    UserListAdapter adapter = new UserListAdapter(getActivity().getApplicationContext(), temp);
    setListAdapter(adapter);

    setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        initiateRefresh();
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    initiateRefresh();
  }

  /**
   * Starts a LoadUsersTask in background
   */
  private void initiateRefresh() {
    Log.i(TAG, "initiate event list refresh");
    LoadUsersRequest request = new LoadUsersRequest();
    new LoadUsersTask().execute(request);
  }

  /**
   * Callback from the LoadUsersTask for when the data has been fetched from server
   * @param users list of users in event
   */
  private void onRefreshComplete(List<User> users){
    // Remove all items from the ListAdapter, and then replace them with the new items
    UserListAdapter adapter = (UserListAdapter) getListAdapter();
    adapter.clear();
    for (User user: users) {
      adapter.add(user);
    }

    // Stop the refreshing indicator
    setRefreshing(false);
  }

  /**
   * Handles the display of events in a row
   */
  private class UserListAdapter extends ArrayAdapter<User> {
    private UserListAdapter(Context context, List<User> objects) {
      super(context, R.layout.user_list_row, objects);
    }

    private UserListAdapter(Context context, User[] users) {
      super(context, R.layout.user_list_row,users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      LayoutInflater inflater = LayoutInflater.from(getContext());
      View customView  = inflater.inflate(R.layout.user_list_row, parent, false);

      User userItem = getItem(position);
      TextView rowName = (TextView) customView.findViewById(R.id.user_row_name);
      TextView rowId = (TextView) customView.findViewById(R.id.user_row_id);

      rowName.setText(userItem.getName());
      rowId.setText("id: " + String.valueOf(userItem.getId()));

      return customView;
    }
  }

  /**
   * A background task to fetch events from our server without slowing ui
   */
  private class LoadUsersTask extends AsyncTask<LoadUsersRequest, Void, List<User>> {

    @Override
    protected void onPostExecute(List<User> users) {
      super.onPostExecute(users);

      //post results
      onRefreshComplete(users);
    }
  }
}
