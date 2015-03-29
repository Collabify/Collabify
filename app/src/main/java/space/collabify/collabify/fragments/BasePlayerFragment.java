package space.collabify.collabify.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import space.collabify.collabify.R;

import space.collabify.collabify.managers.AppManager;

/**
 * This file was born on March 11 at 14:11
 */
public class BasePlayerFragment extends Fragment {


        if (!AppManager.getInstance().getUser().getRole().isDJ()) {
            View djControls = rootView.findViewById(R.id.player_dj);
            djControls.setVisibility(View.GONE);
            return null;
        }

        return rootView;
    }
}
