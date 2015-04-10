package space.collabify.collabify.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import space.collabify.collabify.*;
import space.collabify.collabify.base.CollabifyActivity;

/**
 * This file was born on March 11 at 13:59
 */
public class ModeSelectActivity extends CollabifyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode);
    }

    public void toCreateEvent(View view) {
        if (mAppManager.getUser().isPremium()) {
          Intent intent = new Intent(this, CreateEventActivity.class);
          startActivity(intent);
        } else {
          AlertDialog alertDialog = new AlertDialog.Builder(this).create();
          alertDialog.setTitle("No Spotify Premium");
          alertDialog.setMessage("We are sorry, but you need Spotify Premium to access the DJ Mode.");
          alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            });
          alertDialog.show();
        }
    }

    public void toJoinEvent(View view) {
        Intent intent = new Intent(this, JoinEventActivity.class);
        startActivity(intent);
    }
}
