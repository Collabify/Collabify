package space.collabify.collabify.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;

import space.collabify.collabify.*;
import space.collabify.collabify.base.CollabifyActivity;

/**
 * This file was born on March 11 at 14:02
 */
public class CollabifierActivity extends CollabifyActivity implements ActionBar.TabListener{
  private ViewPager viewPager;
  private TabsPagerAdapter mAdapter;
  private ActionBar actionBar;
  // Tab titles
  private String[] tabs = {"Player", "Playlist", "DJ Tracks"};
  private int[] icons = {R.drawable.ic_player, R.drawable.ic_playlist, R.drawable.ic_dj};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_collabifier);

    // Initilization
    viewPager = (ViewPager) findViewById(R.id.collabifierPager);
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
    for (int i=0; i < tabs.length; i++) {
//      actionBar.addTab(actionBar.newTab()
//        .setText(tabs[i])
//        .setIcon(icons[i])
//        .setCustomView(R.layout.tab_layout)
//        .setTabListener(this));
      View tabView = getLayoutInflater().inflate(R.layout.tab_layout, null);
      TextView tabText = (TextView) tabView.findViewById(R.id.tabText);
      tabText.setText(tabs[i]);

      ImageView tabImage = (ImageView) tabView.findViewById(R.id.tabIcon);
      tabImage.setImageDrawable(getResources().getDrawable(icons[i]));

      actionBar.addTab(actionBar.newTab()
        .setCustomView(tabView)
        .setTabListener(this));
    }

  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu){

      getMenuInflater().inflate(R.menu.song_search, menu);

      final MenuItem searchItem = menu.findItem(R.id.action_search);

      final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

      searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View view, boolean queryTextFocused){
              if(!queryTextFocused){
                  searchItem.collapseActionView();
                  searchView.setQuery("", false);
              }
          }
      });

      searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
          @Override
          public boolean onQueryTextSubmit(String query){

              Intent intent = new Intent(CollabifierActivity.this, DetailedSearchActivity.class);

              intent.putExtra("query", query);

              startActivity(intent);

              return true;
          }

          @Override
          public boolean onQueryTextChange(String query){
              return true;
          }
      });

      return super.onCreateOptionsMenu(menu);
  }

  @Override
  public void onBackPressed() {
    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
    // construct the dialog
    builder.setTitle(R.string.title_exit_event);
    builder.setMessage(R.string.label_exit_event);

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
