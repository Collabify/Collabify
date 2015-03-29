package space.collabify.collabify.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.collabify.collabify.R;

/**
 * This file was born on March 11 at 14:11
 */
public class BasePlayerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dj = getView().findViewById(R.id.player_dj);
        dj.setVisibility(View.GONE);

        return null;
    }

}
