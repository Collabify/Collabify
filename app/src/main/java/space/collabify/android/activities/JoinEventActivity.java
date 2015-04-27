package space.collabify.android.activities;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.spotify.sdk.android.authentication.AuthenticationClient;


import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.LocationService;
import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.collabify.models.network.UserDO;
import space.collabify.android.fragments.JoinEventListFragment;
import space.collabify.android.managers.CollabifyCallback;
import space.collabify.android.models.Event;
import space.collabify.android.models.Role;
import space.collabify.android.models.User;

/**
 * This file was born on March 11 at 14:00
 */
public class JoinEventActivity extends CollabifyActivity {
    private static final String TAG = JoinEventActivity.class.getSimpleName();
    private JoinEventListFragment mJoinEventListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = true;

        //request to enable location if not enabled
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }

        //start the location service
        Intent locationIntent = new Intent(this, LocationService.class);
        startService(locationIntent);

        //register for location updates
        registerReceiver(mLocationReceiver, new IntentFilter(LocationService.BROADCAST_INTENT));

        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mJoinEventListFragment = new JoinEventListFragment();
            mJoinEventListFragment.isGpsEnabled = manager.isProviderEnabled( LocationManager.GPS_PROVIDER );
            transaction.replace(R.id.event_list_frame, mJoinEventListFragment, TAG);
            transaction.commit();
        }else {
            //TODO: get join event fragment reference from savedInstanceState?
        }

    }

    //check it out here: http://stackoverflow.com/questions/843675/how-do-i-find-out-if-the-gps-of-an-android-device-is-enabled
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please enable GPS to see events!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //update list
        //TODO: find less hacky way to do this
        //without the post delayed, it doesn't work on the
        //the first call, but it does on subsequent
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mJoinEventListFragment != null)
                    mJoinEventListFragment.initializeList(null);
            }
        }, 10);
    }


    public void toCollabifier(Event event, String password){
        if (event.isProtectedEvent()) {
            if (event.getPassword() == null) {
                Toast.makeText(JoinEventActivity.this, "There is an error with this event. Sorry :(", Toast.LENGTH_LONG).show();
                return;
            }
            else if (!event.getPassword().equals(password)) {
                Toast.makeText(JoinEventActivity.this, "Bad Password!", Toast.LENGTH_LONG).show();
                return;
            }
        }

        mAppManager.joinEvent(event.getId(), new CollabifyCallback<Event>() {
            @Override
            public void success(Event event, Response response) {
                Log.d(TAG, "Successfully joined");
                Intent intent = new Intent(JoinEventActivity.this, CollabifierActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(TAG, "Failed to join event:\n" + error.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(JoinEventActivity.this, "Error joining Event. Please try again!", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void exception(Exception e) {
                Log.e(TAG, "Failed to join event:\n" + e.getClass().getSimpleName());

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(JoinEventActivity.this, "Error joining Event. Please try again!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void toCreateEvent(View v) {
        if (mAppManager.getUser().isPremium()) {
            Intent intent = new Intent(this, CreateEventActivity.class);
            startActivity(intent);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("No Spotify Premium");
            alertDialog.setMessage("We are sorry, but you need a Spotify Premium account to create events.");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Dismiss",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Upgrade",
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Uri url = Uri.parse("https://www.spotify.com/us/premium/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, url);
                    startActivity(intent);
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        }
    }


    private BroadcastReceiver mLocationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra(LocationService.BUNDLE_EXTRA);
            Location location = bundle.getParcelable(LocationService.PARCEL_LOCATION);
            mJoinEventListFragment.initializeList(location);
        }
    };

}
