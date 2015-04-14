package space.collabify.android.activities;


import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;


import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import space.collabify.android.R;
import space.collabify.android.base.CollabifyActivity;
import space.collabify.android.collabify.models.network.UserDO;
import space.collabify.android.fragments.JoinEventListFragment;
import space.collabify.android.models.Event;
import space.collabify.android.models.Role;
import space.collabify.android.models.User;

/**
 * This file was born on March 11 at 14:00
 */
public class JoinEventActivity extends CollabifyActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = JoinEventActivity.class.getSimpleName();
    private JoinEventListFragment mJoinEventListFragment;
    private GoogleApiClient mGoogleApiClient;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //update location
            mJoinEventListFragment.updateLocation(location);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);

        SHOW_SETTINGS = true;
        SHOW_LEAVE = false;
        SHOW_LOGOUT = true;

        if(savedInstanceState == null){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            mJoinEventListFragment = new JoinEventListFragment();
            transaction.replace(R.id.event_list_frame, mJoinEventListFragment, TAG);
            transaction.commit();
        }else {
            //TODO: get join event fragment reference from savedInstanceState?
        }
        //set up location services
        buildGoogleApiClient();
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
                mJoinEventListFragment.initializeList();
            }
        }, 10);
    }

    /**
     * Initializes the google api for location services
     */
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void toCollabifier(Event event, String password){
        //TODO may have to change how password is handled/displayed
        if(event.getPassword().equalsIgnoreCase(password)){
            mAppManager.getUser().setRole(Role.COLLABIFIER);

          mAppManager.joinEvent(event,
            new Callback<space.collabify.android.collabify.models.domain.User> () {
              @Override
              public void success(space.collabify.android.collabify.models.domain.User user, Response response) {
                User current = mAppManager.getUser();
                if (current.getId().equals(user.getUserId())) {
                  Log.d(TAG, "Successfully joined");
                  Intent intent = new Intent(JoinEventActivity.this, CollabifierActivity.class);
                  startActivity(intent);
                }
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
            }
          );

        } else {
            //bad password, don't do anything
            Toast.makeText(JoinEventActivity.this, "Bad Password!", Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onConnected(Bundle connectionHint) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(lastLocation != null){
            mJoinEventListFragment.updateLocation(lastLocation);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "google api location services suspended");

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.w(TAG, "google api location services connection failed");
    }
}
