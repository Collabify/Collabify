package space.collabify.collabify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.ListFragment;

/**
 * Created by andrew on 3/20/15.
 */
public class JoinEventListFragment extends ListFragment {

  public View inflateFragment(int i, LayoutInflater layoutInflater, ViewGroup viewGroup) {
    return null;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    String[] values = new String[]{"Android", "iPhone", "WindowsMobile",
      "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
      "Linux", "OS/2", "More", "List", "Items", "Here"};
    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
      android.R.layout.simple_list_item_1, values);
    setListAdapter(adapter);
  }
}
