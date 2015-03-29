package space.collabify.collabify.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyFragment;

import space.collabify.collabify.managers.AppManager;

/**
 * This file was born on March 11 at 14:11
 */
public class BasePlayerFragment extends CollabifyFragment {

  @Override
  public View inflateFragment(int i, LayoutInflater layoutInflater, ViewGroup viewGroup) {
    return null;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_base_player, container, false);

    if (!AppManager.getInstance().getUser().getRole().isDJ()) {
      View djControls = rootView.findViewById(R.id.player_dj);
      djControls.setVisibility(View.GONE);
    }

    return rootView;
  }
}
