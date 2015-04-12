package space.collabify.android;

/**
 * Created by andrew on 3/26/15.
 * Used code from: http://www.androidhive.info/2013/10/android-tab-layout-with-swipeable-views-1/
 */

import space.collabify.android.fragments.BasePlayerFragment;
import space.collabify.android.fragments.DjTracksFragment;
import space.collabify.android.fragments.PlaylistFragment;
import space.collabify.android.fragments.UserListFragment;
import space.collabify.android.managers.AppManager;
import space.collabify.android.models.Role;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;


/**
 * uses some code from http://stackoverflow.com/questions/8785221/retrieve-a-fragment-from-a-viewpager
 * for allowing access to a fragment
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {
    private Role mRole;
    private Fragment mCurrentFragment;

    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    public TabsPagerAdapter(FragmentManager fm, Role role) {
        super(fm);
        this.mRole = role;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Base Player fragment activity
                return new BasePlayerFragment();
            case 1:
                // Playlist fragment activity
                return new PlaylistFragment();
            case 2:
                // DJ Tracks fragment activity
                return new DjTracksFragment();
            case 3:
                // User List fragment activity
                return new UserListFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        if (AppManager.getInstance().getUser().getRole().isDJ()) {
            return 4;
        } else {
            return 3;
        }
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }

    @Override
    public void startUpdate(ViewGroup container) {
        super.startUpdate(container);
    }

    @Override
    public void finishUpdate(ViewGroup container) {
        super.finishUpdate(container);
    }
}
