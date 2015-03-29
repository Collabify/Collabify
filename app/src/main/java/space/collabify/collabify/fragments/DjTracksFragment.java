package space.collabify.collabify.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyFragment;


/**
 * This file was born on March 11, at 15:53
 */
public class DjTracksFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_dj_tracks, container, false);
    return rootView;
  }
}
