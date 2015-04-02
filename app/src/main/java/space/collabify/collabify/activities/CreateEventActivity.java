package space.collabify.collabify.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;

import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Role;

/**
 * This file was born on March 11 at 14:00
 */
public class CreateEventActivity extends CollabifyActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
    }

    public void toDj(View view) {
      mAppManager.getUser().setRole(Role.DJ);

      EditText mName = (EditText) findViewById(R.id.event_field);
      CheckBox mPasswordProtect = (CheckBox) findViewById(R.id.password_protected_checkbox);
      EditText mPassword = (EditText) findViewById(R.id.password_field);
      CheckBox mAllowFeedback = (CheckBox) findViewById(R.id.allow_feedback_checkbox);
      CheckBox mRestrictNearby = (CheckBox) findViewById(R.id.restrict_nearby_checkbox);

      String name = mName.getText().toString();
      boolean passwordProtect = mPasswordProtect.isChecked();
      String password = mPassword.getText().toString();
      boolean allowFeedback = mAllowFeedback.isChecked();
      boolean restrictNearby = mRestrictNearby.isChecked();

      mAppManager.createEvent(new Event(name, 0, password, allowFeedback, restrictNearby));
      Intent intent = new Intent(this, DjActivity.class);
      startActivity(intent);
    }
}
