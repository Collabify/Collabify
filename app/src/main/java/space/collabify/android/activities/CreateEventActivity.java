package space.collabify.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Event;
import space.collabify.android.models.Role;

/**
 * This file was born on March 11 at 14:00
 */
public class CreateEventActivity extends CollabifyActivity {
    private static final String TAG = JoinEventActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = true;

        EditText mName = (EditText) findViewById(R.id.event_field);
        mName.setText("Test123");
    }

    public void toDj(View view) {
        mAppManager.getUser().setRole(Role.DJ);

        EditText mName = (EditText) findViewById(R.id.event_field);
        EditText mPassword = (EditText) findViewById(R.id.password_field);
        CheckBox mPasswordProtected = (CheckBox) findViewById(R.id.password_protected_checkbox);
        CheckBox mAllowFeedback = (CheckBox) findViewById(R.id.allow_feedback_checkbox);
        CheckBox mRestrictNearby = (CheckBox) findViewById(R.id.restrict_nearby_checkbox);

        String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        boolean passwordProtected = mPasswordProtected.isChecked();
        boolean allowFeedback = mAllowFeedback.isChecked();

        if (password.equals("") && passwordProtected) {
            mPassword.setError("Please enter a password");
        }
        else if (name.equals("")) {
            mName.setError("Please enter an event name");
        }
        else {
            mPassword.setError(null);
            mName.setError(null);

            mName.setText("");
            mPassword.setText("");
            mPasswordProtected.setChecked(false);
            mPassword.setText("");
            mAllowFeedback.setChecked(false);



            mAppManager.createEvent(new Event(name, mAppManager.getUser().getId(), password, allowFeedback), new CollabifyCallback<space.collabify.android.collabify.models.domain.Event>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.Event event, Response response) {
                    Log.d(TAG, "Successfully created");
                    Intent intent = new Intent(CreateEventActivity.this, DjActivity.class);
                    startActivity(intent);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "Failed to create event:\n" + error.toString());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CreateEventActivity.this, "Error creating Event. Please try again!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void exception(Exception e) {
                    Log.e(TAG, "Failed to create the event.");
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CreateEventActivity.this, "Error creating Event. Please try again!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });

        }
    }
}
