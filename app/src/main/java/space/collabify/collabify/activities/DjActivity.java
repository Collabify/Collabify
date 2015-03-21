package space.collabify.collabify.activities;

import android.os.Bundle;
import android.widget.TabHost;

import space.collabify.collabify.*;

/**
 * This file was born on March 11 at 14:02
 */
public class DjActivity extends PrimaryViewActivity {
    private TabHost myTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        myTabHost = (TabHost) findViewById(R.id.dj_tab_host);
        // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup();

        // Adding tabs
        TabHost.TabSpec tab1 = myTabHost.newTabSpec("tab_creation");
        TabHost.TabSpec tab2 = myTabHost.newTabSpec("tab_creation");
        TabHost.TabSpec tab3 = myTabHost.newTabSpec("tab_creation");
        TabHost.TabSpec tab4 = myTabHost.newTabSpec("tab_creation");

        // text and image of tab
        tab1.setIndicator("Player", getResources().getDrawable(android.R.drawable.ic_menu_add));
        tab2.setIndicator("Playlist", getResources().getDrawable(android.R.drawable.ic_menu_add));
        tab3.setIndicator("Users", getResources().getDrawable(android.R.drawable.ic_menu_add));
        tab4.setIndicator("DJ Tracks", getResources().getDrawable(android.R.drawable.ic_menu_add));

        // specify layout of tab
        tab1.setContent(R.id.dj_tab1);
        tab2.setContent(R.id.dj_tab2);
        tab3.setContent(R.id.dj_tab3);
        tab4.setContent(R.id.dj_tab4);

        // adding tab in TabHost
        myTabHost.addTab(tab1);
        myTabHost.addTab(tab2);
        myTabHost.addTab(tab3);
        myTabHost.addTab(tab4);
    }
}