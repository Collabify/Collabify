package space.collabify.collabify.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.TabHost;

import space.collabify.collabify.*;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.fragments.CollabifierPlaylistFragment;

/**
 * This file was born on March 11 at 14:02
 */
public class CollabifierActivity extends CollabifyActivity {
    private FragmentTabHost myTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collabifier);

        myTabHost = (FragmentTabHost) findViewById(R.id.collabifier_tab_host);
        // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup(this, getSupportFragmentManager());

        // Adding tabs
        TabHost.TabSpec tab1 = myTabHost.newTabSpec("tab_creation");
        TabHost.TabSpec tab2 = myTabHost.newTabSpec("tab_creation");
        TabHost.TabSpec tab3 = myTabHost.newTabSpec("tab_creation");

        // text and image of tab
        tab1.setIndicator("Player", getResources().getDrawable(android.R.drawable.ic_menu_add));
        tab2.setIndicator("Playlist", getResources().getDrawable(android.R.drawable.ic_menu_add));
        tab3.setIndicator("DJ Tracks", getResources().getDrawable(android.R.drawable.ic_menu_add));

        // specify layout of tab
        //tab1.setContent(R.id.collabifier_tab1);
        //tab2.setContent(R.id.collabifier_tab2);
        //tab3.setContent(R.id.collabifier_tab3);

        // adding tab in TabHost
        myTabHost.addTab(tab1);
        myTabHost.addTab(tab2, CollabifierPlaylistFragment.class, null);
        myTabHost.addTab(tab3);

        View djControls = findViewById(R.id.player_dj);
        djControls.setVisibility(View.GONE);

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
}
