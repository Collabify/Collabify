package space.collabify.collabify;

/**
 * Created by andrew on 3/26/15.
 * Used code from: http://www.androidhive.info/2013/10/android-tab-layout-with-swipeable-views-1/
 */

import space.collabify.collabify.fragments.BasePlayerFragment;
import space.collabify.collabify.fragments.DjTracksFragment;
import space.collabify.collabify.fragments.PlaylistFragment;
import space.collabify.collabify.fragments.UserListFragment;
import space.collabify.collabify.managers.AppManager;
import space.collabify.collabify.models.Role;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

  public TabsPagerAdapter(FragmentManager fm) {
    super(fm);
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
    if (AppManager.getInstance().getUser().getRole().equals(Role.DJ)) {
      return 4;
    } else {
      return 3;
    }
  }

}
