package space.collabify.collabify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import space.collabify.collabify.*;

/**
 * This file was born on March 11 at 14:00
 */
public class CreateEventActivity extends CollabifyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    public void toModeSelect(View view) {
      finish();
    }

    public void toDj(View view) {
      Intent intent = new Intent(this, DjActivity.class);
      startActivity(intent);
    }
}
