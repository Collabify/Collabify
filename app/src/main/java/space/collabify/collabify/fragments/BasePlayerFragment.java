package space.collabify.collabify.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import space.collabify.collabify.R;

/**
 * This file was born on March 11 at 14:11
 */
public class BasePlayerFragment extends CollabifyFragment {

  @Override
  public View inflateFragment(int i, LayoutInflater layoutInflater, ViewGroup viewGroup) {
    View dj = getView().findViewById(R.id.player_dj);
    dj.setVisibility(View.GONE);

    return null;
  }
}
