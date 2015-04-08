package space.collabify.collabify.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.CollabifyClient;
import space.collabify.collabify.requests.UsersRequest;
import space.collabify.collabify.R;
import space.collabify.collabify.models.Role;
import space.collabify.collabify.models.User;

/**
 * This file was born on March 11, at 15:52
 */
public class UserListFragment extends SwipeRefreshListFragment {
    private static final String TAG = UserListFragment.class.getSimpleName();

    List<User> userlist;
    UserListAdapter adapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User tmpUser = new User("Waiting for server", 9999);
        List<User> temp = new ArrayList<>();
        temp.add(tmpUser);
        adapter = new UserListAdapter(getActivity().getApplicationContext(), temp);
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
        UsersRequest request = new UsersRequest();
        new LoadUsersTask().execute(request);
    }

    /**
     * Callback from the LoadUsersTask for when the data has been fetched from server
     *
     * @param users list of users in event
     */
    private void onRefreshComplete(List<User> users) {
        // Remove all items from the ListAdapter, and then replace them with the new items
        UserListAdapter adapter = (UserListAdapter) getListAdapter();
        adapter.clear();
        for (User user : users) {
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
            userlist = objects;
        }

        private UserListAdapter(Context context, User[] users) {
            super(context, R.layout.user_list_row, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View customView = inflater.inflate(R.layout.user_list_row, parent, false);

            User userItem = getItem(position);
            TextView rowName = (TextView) customView.findViewById(R.id.user_row_name);
            ImageView rowIcon = (ImageView) customView.findViewById(R.id.user_row_icon);

            rowName.setText(userItem.getName());

            if (userlist.get(0).getId() != 9999) {
              switch (userlist.get(position).getRole().getRole()) {
                case Role.PROMOTED:
                  rowIcon.setImageResource(R.drawable.promoted_user);
                  break;
                case Role.BLACKLISTED:
                  rowIcon.setImageResource(R.drawable.blacklisted_icon);
                  break;
                case Role.COLLABIFIER:
                  rowIcon.setImageResource(R.drawable.collabifier_icon);
                  break;
              }
            }

            return customView;
        }
    }

    /**
     * A background task to fetch users from our server without slowing ui
     */
    private class LoadUsersTask extends AsyncTask<UsersRequest, Void, List<User>> {
        @Override
        protected List<User> doInBackground(UsersRequest... params) {
            return CollabifyClient.getInstance().getUsers(params[0]);
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);

            //post results
            onRefreshComplete(users);

          userlist = users;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
      final User u = userlist.get(position);

      final String[] roles = {Role.PROMOTED, Role.COLLABIFIER, Role.BLACKLISTED};

      int rolePos = -1;
      switch(u.getRole().getRole()) {
        case Role.PROMOTED:
          rolePos = 0;
          break;
        case Role.COLLABIFIER:
          rolePos = 1;
          break;
        case Role.BLACKLISTED:
          rolePos = 2;
          break;
      }

      if (rolePos != -1) {
        roles[rolePos] += " (Current)";
      }

      if (!CollabifyClient.getInstance().isUsersUpdating()) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Change User Role:");
        builder.setItems(roles, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int item) {
            // Do something with the selection if not same as current role
            String current = u.getRole().getRole() + " (Current)";
            if (!current.equals(roles[item])) {
              Toast.makeText(getActivity(), u.getName() + " changed to " + roles[item], Toast.LENGTH_SHORT).show();
              u.getRole().setRole(roles[item]);
              adapter.notifyDataSetChanged();
            }
          }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        AlertDialog alert = builder.create();
        alert.show();
      }
    }
}
