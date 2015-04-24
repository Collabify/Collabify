package space.collabify.android.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.collabify.android.R;


/**
 * This file was born on March 11, at 15:53
 */
public class DjTracksFragment extends ListFragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_dj_tracks, container, false);
    return rootView;
  }
}
