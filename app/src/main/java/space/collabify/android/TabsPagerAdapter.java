package space.collabify.android;

/**
 * Created by andrew on 3/26/15.
 * Used code from: http://www.androidhive.info/2013/10/android-tab-layout-with-swipeable-views-1/
 */

import space.collabify.android.fragments.BasePlayerFragment;
import space.collabify.android.fragments.DjTracksFragment;
import space.collabify.android.fragments.PlaylistFragment;
import space.collabify.android.fragments.UserListFragment;
import space.collabify.android.models.Role;

import android.content.Context;
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
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    private Fragment mCurrentFragment;
    private Context mContext;
    private Role mUserRole;

    public TabsPagerAdapter(FragmentManager fm, Context context, Role userRole) {
        super(fm);
        this.mContext = context;
        this.mUserRole = userRole;
    }

    @Override
    public Fragment getItem(int index) {
        Fragment fragment = null;

        // DJ Tabs
        if (mUserRole.isDJ()) {
            switch (index) {
                case 0:
                    // DJ Player fragment
                    fragment = Fragment.instantiate(mContext, BasePlayerFragment.class.getName());
                    break;
                case 1:
                    // Playlist Fragment
                    fragment = Fragment.instantiate(mContext, PlaylistFragment.class.getName());
                    break;
                case 2:
                    // DJ Tracks Fragment
                    fragment = Fragment.instantiate(mContext, DjTracksFragment.class.getName());
                    break;
                case 3:
                    // User List Fragment
                    fragment = Fragment.instantiate(mContext, UserListFragment.class.getName());
                    break;
            }
        }
        // Collabifier Tabs
        else {
            switch (index) {
                case 0:
                    // Playlist Fragment
                    fragment = Fragment.instantiate(mContext, PlaylistFragment.class.getName());
                    break;
                case 1:
                    // DJ Tracks Fragment
                    fragment = Fragment.instantiate(mContext, DjTracksFragment.class.getName());
                    break;
            }
        }

        return fragment;
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return (mUserRole.isDJ()) ? 4 : 2;
    }


    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            mCurrentFragment = ((Fragment) object);

            if(mCurrentFragment instanceof BasePlayerFragment){
                ((BasePlayerFragment)mCurrentFragment).updatePlayerView();
            }
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
