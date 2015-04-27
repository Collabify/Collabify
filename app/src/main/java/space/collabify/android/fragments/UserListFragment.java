package space.collabify.android.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.collabify.api.CollabifyApi;
import space.collabify.android.collabify.api.CollabifyApiException;
import space.collabify.android.collabify.models.Converter;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.requests.UsersRequest;
import space.collabify.android.R;
import space.collabify.android.models.Role;
import space.collabify.android.models.User;

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

        User tmpUser = new User("Waiting for server", "9999");
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
        setRefreshing(true);
        AppManager.getInstance().loadEventUsers(new CollabifyCallback<List<User>>() {
            @Override
            public void exception(Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshing(false);
                    }
                });
            }

            @Override
            public void success(List<User> users, Response response) {
                //post results
                final List<User> temp = users;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        onRefreshComplete(temp);
                        setRefreshing(false);
                    }
                });

                userlist = users;
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRefreshing(false);
                    }
                });
            }
        });
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

        if (users.size() != 0) {
          for (User user : users) {
            adapter.add(user);
          }
        } else {
          adapter.add(new User("No one is at your event :(", "9999"));
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

          if (userlist.size() != 0) {
            switch (userlist.get(position).getRole().getRole()) {
              case Role.PROMOTED:
                rowIcon.setImageResource(R.drawable.ic_promoted);
                break;
              case Role.BLACKLISTED:
                rowIcon.setImageResource(R.drawable.ic_blacklisted);
                break;
              case Role.COLLABIFIER:
                rowIcon.setImageResource(R.drawable.ic_collabifier);
                break;
              default:
                break;
            }
          }


            return customView;
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
      final String[] roles = {Role.PROMOTED, Role.COLLABIFIER, Role.BLACKLISTED};
      final Integer[] icons = {R.drawable.ic_promoted, R.drawable.ic_collabifier, R.drawable.ic_blacklisted};

      if (userlist.size() != 0) {
        final User u = userlist.get(position);

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

        if (!AppManager.getInstance().isUsersUpdating()) {

          ListAdapter ladapter = new ArrayAdapterWithIcon(getActivity(), roles, icons);

          new AlertDialog.Builder(getActivity()).setTitle("Change User Role")
            .setAdapter(ladapter, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection if not same as current role
                final String newrole = roles[item];
                String current = u.getRole().getRole() + " (Current)";
                AppManager.getInstance().changeUserRole(u, newrole, new CollabifyCallback<space.collabify.android.collabify.models.domain.Role>() {
                  @Override
                  public void success(space.collabify.android.collabify.models.domain.Role role, Response response) {
                    getActivity().runOnUiThread(new Runnable() {
                      public void run() {
                        Toast.makeText(getActivity(), u.getName() + " changed to " + newrole, Toast.LENGTH_SHORT).show();
                      }
                    });

                      u.setRole(role.getRole());
                      adapter.notifyDataSetChanged();
                  }

                    @Override
                    public void failure (RetrofitError retrofitError){
                      retrofitError.printStackTrace();
                      getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                          Toast.makeText(getActivity(), "Error changing " + u.getName() + "'s role", Toast.LENGTH_SHORT).show();
                        }
                      });

                    }

                    @Override
                    public void exception (Exception e){
                      e.printStackTrace();
                      getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                          Toast.makeText(getActivity(), "Error changing " + u.getName() + "'s role", Toast.LENGTH_SHORT).show();
                        }
                      });
                    }
                  }

                  );
                }
              }

              ).

              setNegativeButton(android.R.string.cancel, null)

              .

              show();
            }
        }
    }
}
