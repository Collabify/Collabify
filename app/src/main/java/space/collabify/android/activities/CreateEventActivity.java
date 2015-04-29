package space.collabify.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
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
import space.collabify.android.LocationService;
import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.managers.AppManager;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Event;
import space.collabify.android.models.Role;

/**
 * This file was born on March 11 at 14:00
 */
public class CreateEventActivity extends CollabifyActivity {
    private static final String TAG = CreateEventActivity.class.getSimpleName();
    private static ProgressDialog progress;

    private static CheckBox mPasswordProtected;
    private static TextView mPasswordLabel;
    private static EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = true;

        EditText mName = (EditText) findViewById(R.id.event_field);

        if (mAppManager.getUser().getName().equals("")) {
          mName.setText(mAppManager.getUser().getId() + "'s Event");
        } else {
          mName.setText(mAppManager.getUser().getName() + "'s Event");
        }

        mPasswordProtected = (CheckBox) findViewById(R.id.password_protected_checkbox);
        mPasswordLabel = (TextView) findViewById(R.id.password);
        mPasswordField = (EditText) findViewById(R.id.password_field);

        mPasswordProtected.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            if (((CheckBox) v).isChecked()) {
              mPasswordLabel.setVisibility(View.VISIBLE);
              mPasswordField.setVisibility(View.VISIBLE);
            } else {
              mPasswordLabel.setVisibility(View.INVISIBLE);
              mPasswordField.setVisibility(View.INVISIBLE);
            }
          }
        });
    }

    public void toDj(View view) {
        mAppManager.getUser().setRole(Role.DJ);

        EditText mName = (EditText) findViewById(R.id.event_field);
        EditText mPassword = (EditText) findViewById(R.id.password_field);
        CheckBox mPasswordProtected = (CheckBox) findViewById(R.id.password_protected_checkbox);
        CheckBox mAllowFeedback = (CheckBox) findViewById(R.id.allow_feedback_checkbox);
//        CheckBox mRestrictNearby = (CheckBox) findViewById(R.id.restrict_nearby_checkbox);

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
            progress = ProgressDialog.show(this, "Creating your event",  "Assembing the party!", true);

            mPassword.setError(null);
            mName.setError(null);

            Event djEvent = new Event(name, mAppManager.getUser().getId(), password, allowFeedback);
            Location userLocation = AppManager.getInstance().getLastKnownLocation();
            if(userLocation == null){
                Log.e(TAG, "user location was null when trying to create event");
                Toast.makeText(getApplicationContext(), "Unable to find location, please enable location", Toast.LENGTH_LONG).show();
                progress.dismiss();

                //start the location service
                Intent locationIntent = new Intent(this, LocationService.class);
                startService(locationIntent);
                return;
            }
            djEvent.setLatitude(Double.toString(userLocation.getLatitude()));
            djEvent.setLongitude(Double.toString(userLocation.getLongitude()));

            mAppManager.createEvent(djEvent, new CollabifyCallback<space.collabify.android.collabify.models.domain.Event>() {
                @Override
                public void success(space.collabify.android.collabify.models.domain.Event event, Response response) {
                    progress.dismiss();
                    Log.d(TAG, "Successfully created");

                    runOnUiThread(new Runnable() {
                      public void run() {
                        ((EditText) findViewById(R.id.event_field)).setText("");
                        ((EditText) findViewById(R.id.password_field)).setText("");
                        ((CheckBox) findViewById(R.id.password_protected_checkbox)).setChecked(false);
                        ((CheckBox) findViewById(R.id.allow_feedback_checkbox)).setChecked(false);
                      }
                    });

                    Intent intent = new Intent(CreateEventActivity.this, DjActivity.class);
                    startActivity(intent);
                }

                @Override
                public void failure(RetrofitError error) {
                    progress.dismiss();
                    Log.e(TAG, "Failed to create event:\n" + error.toString());

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CreateEventActivity.this, "Error creating Event. Please try again!", Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void exception(Exception e) {
                    progress.dismiss();
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
