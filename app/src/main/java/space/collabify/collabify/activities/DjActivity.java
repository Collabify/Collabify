package space.collabify.collabify.activities;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import space.collabify.collabify.*;
import space.collabify.collabify.base.CollabifyActivity;

/**
 * This file was born on March 11 at 14:02
 */
public class DjActivity extends CollabifyActivity implements ActionBar.TabListener {
  private ViewPager viewPager;
  private TabsPagerAdapter mAdapter;
  private ActionBar actionBar;
  // Tab titles
  private String[] tabs = {"Player", "Playlist", "DJ Tracks", "User List"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dj);

    // Initilization
    viewPager = (ViewPager) findViewById(R.id.djPager);
    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        // on changing the page
        // make respected tab selected
        actionBar.setSelectedNavigationItem(position);
        }

      @Override
      public void onPageScrolled(int arg0, float arg1, int arg2) {
      }

      @Override
      public void onPageScrollStateChanged(int arg0) {
      }
    });


    actionBar = getSupportActionBar();
    mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

    viewPager.setAdapter(mAdapter);
    actionBar.setHomeButtonEnabled(false);
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Adding Tabs
    for (String tab_name : tabs) {
      actionBar.addTab(actionBar.newTab().setText(tab_name)
        .setTabListener(this));
    }

  }

  @Override
  public void onBackPressed() {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    // construct the dialog
    builder.setTitle(R.string.title_end_event);
    builder.setMessage(R.string.label_end_event);

    // exit if OK button pressed
    builder.setPositiveButton(getString(R.string.ok_button),
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          finish();
        }
      }
    );

    // close dialog on Cancel button pressed
    builder.setNegativeButton(getString(R.string.cancel_button),
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      }
    );

    builder.show();
  }

  @Override
  public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
    viewPager.setCurrentItem(tab.getPosition());
  }

  @Override
  public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
  }

  @Override
  public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
  }
}