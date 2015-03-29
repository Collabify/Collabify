package space.collabify.collabify.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import space.collabify.collabify.R;
import space.collabify.collabify.models.User;

/**
 * This file was born on March 11, at 15:52
 */
public class UserListFragment extends SwipeRefreshListFragment {
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
        //initiateRefresh();
      }
    });
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
      rowId.setText(String.valueOf(userItem.getId()));

      return customView;
    }
  }
}
