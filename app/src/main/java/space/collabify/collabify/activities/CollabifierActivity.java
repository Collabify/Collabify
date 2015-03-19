package space.collabify.collabify.activities;
import android.os.Bundle;
import android.widget.TabHost;

import space.collabify.collabify.*;
/**
 * This file was born on March 11 at 14:02
 */
public class CollabifierActivity extends PrimaryViewActivity {
  private TabHost myTabHost;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_collabifier);

    myTabHost =(TabHost) findViewById(R.id.TabHost01);
    // Before adding tabs, it is imperative to call the method setup()
    myTabHost.setup();

    // Adding tabs
    TabHost.TabSpec tab1 = myTabHost.newTabSpec("tab_creation");
    TabHost.TabSpec tab2 = myTabHost.newTabSpec("tab_creation");

    // text and image of tab
    tab1.setIndicator("Player",getResources().getDrawable(android.R.drawable.ic_menu_add));
    tab2.setIndicator("List",getResources().getDrawable(android.R.drawable.ic_menu_add));

    // specify layout of tab
    tab1.setContent(R.id.tab1);
    tab2.setContent(R.id.tab2);

    // adding tab in TabHost
    myTabHost.addTab(tab1);
    myTabHost.addTab(tab2);

  }

}
