package space.collabify.collabify.activities;


import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;

import space.collabify.collabify.R;
import space.collabify.collabify.fragments.JoinEventListFragment;

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
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            mJoinEventListFragment = new JoinEventListFragment();
            transaction.replace(R.id.event_list_frame, mJoinEventListFragment);
            transaction.commit();
        }else {
            //TODO: get join event fragment reference
        }
        //set up location services
        buildGoogleApiClient();
    }

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void toModeSelect(View view) {
        finish();
    }

    public void toCollabifier(View view) {
        Intent intent = new Intent(this, CollabifierActivity.class);
        startActivity(intent);
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
