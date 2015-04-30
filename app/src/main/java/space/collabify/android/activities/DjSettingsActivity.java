package space.collabify.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.collabify.models.domain.EventSettings;
import space.collabify.android.managers.CollabifyResponseCallback;
import space.collabify.android.collabify.models.domain.Event;

public class DjSettingsActivity extends CollabifyActivity { //BaseSettingsActivity

    private static CheckBox mPasswordProtected;
    private static TextView mPasswordLabel;
    private static EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj_settings);

        SHOW_SETTINGS = false;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = false;

        // auto fill settings
        EditText mName = (EditText) findViewById(R.id.event_name_field);
        Event mEvent = mAppManager.getEvent();
        String eventName = mEvent.getName();
        mName.setText(eventName);

        CheckBox mAllowUserFeedback = (CheckBox) findViewById(R.id.DJ_settings_allow_feedback_button);
        mPasswordProtected = (CheckBox) findViewById(R.id.DJ_settings_password_protected_checkbox);
        mPasswordLabel = (TextView) findViewById(R.id.DJ_settings_password);
        mPasswordField = (EditText) findViewById(R.id.password_field);

        if (mEvent.getSettings() != null) {
          mAllowUserFeedback.setChecked(mEvent.getSettings().isAllowVoting());
          boolean hasPass = !mEvent.getSettings().getPassword().equals("");

          if (hasPass) {
            mPasswordProtected.setChecked(hasPass);
            mPasswordField.setText(mEvent.getSettings().getPassword());
            mPasswordLabel.setVisibility(View.VISIBLE);
            mPasswordField.setVisibility(View.VISIBLE);
          }
        }

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

    public void toAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void updateDJEvent(View view) {
        //mAppManager.getUser().setRole(Role.DJ);

        Event mEvent = mAppManager.getEvent();
        String eventId = mEvent.getEventId();

        EditText mName = (EditText) findViewById(R.id.event_name_field);
        EditText mPassword = (EditText) findViewById(R.id.password_field);
        CheckBox mPasswordProtected = (CheckBox) findViewById(R.id.DJ_settings_password_protected_checkbox);
        CheckBox mAllowFeedback = (CheckBox) findViewById(R.id.DJ_settings_allow_feedback_button);
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
            mPassword.setError(null);
            mName.setError(null);

            mEvent.setName(name);

            EventSettings settings = mEvent.getSettings();

            // not sure if mEvent's settings are being populated
            if (settings != null) {
                settings.setPassword(password);

                settings.setAllowVoting(allowFeedback);
                settings.setLocationRestricted(passwordProtected);

                mEvent.setSettings(settings);

                mAppManager.updateEvent(settings, null);

            }
            // exit settings page after applying settings
            finish();
        }
    }



    public void toLogOut(View view) {

        final Intent intent = new Intent(this, LoginScreenActivity.class);

        mAppManager.logout(getApplicationContext(), new CollabifyResponseCallback() {
            @Override
            public void exception(Exception e) {

            }

            @Override
            public void success(Response response) {
                //return to login activity
                startActivity(intent);
                finish();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

}
