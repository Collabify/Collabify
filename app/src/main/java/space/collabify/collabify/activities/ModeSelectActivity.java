package space.collabify.collabify.activities;

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
        Intent intent = new Intent(this, CreateEventActivity.class);
        startActivity(intent);
    }

    public void toJoinEvent(View view) {
        Intent intent = new Intent(this, JoinEventActivity.class);
        startActivity(intent);
    }
}
