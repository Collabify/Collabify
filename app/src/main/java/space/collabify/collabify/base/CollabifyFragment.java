package space.collabify.collabify.base;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

/**
 * This file was born on March 11 at 14:05
 */
public abstract class CollabifyFragment extends Fragment {
    public abstract View inflateFragment(int i, LayoutInflater layoutInflater, ViewGroup viewGroup);
}