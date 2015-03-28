package space.collabify.collabify.fragments;

import android.os.Bundle;
import android.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyFragment;


/**
 * This file was born on March 11, at 15:52
 */
public class PlaylistFragment extends ListFragment{
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_playlist, container, false);
    return rootView;
  }
}
