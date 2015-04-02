package space.collabify.collabify.activities;


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


import space.collabify.collabify.R;
import space.collabify.collabify.base.CollabifyActivity;
import space.collabify.collabify.fragments.JoinEventListFragment;
import space.collabify.collabify.models.Event;
import space.collabify.collabify.models.Role;

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

    public void toCollabifier(Event event) {
        //TODO: work out exact communication with servermanager
        mAppManager.getUser().setRole(Role.COLLABIFIER);
        mCollabifyClient.joinEvent(event, mAppManager.getUser());
        Intent intent = new Intent(this, CollabifierActivity.class);
        startActivity(intent);
    }

    public void toCollabifier(Event event, String password){
        //TODO may have to change how password is handled/displayed
        if(event.getPassword().equalsIgnoreCase(password)){
            mAppManager.getUser().setRole(Role.COLLABIFIER);
            mCollabifyClient.joinEvent(event, mAppManager.getUser());
            Intent intent = new Intent(this, CollabifierActivity.class);
            startActivity(intent);
        }else {
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
