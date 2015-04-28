package space.collabify.android;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import space.collabify.android.managers.AppManager;

public class LocationService extends Service implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String BROADCAST_INTENT = "LocationServices";
    public static final String BUNDLE_EXTRA = "Location";
    public static final String PARCEL_LOCATION = "Location";
    private static final String TAG = LocationService.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;


    @Override
    public void onCreate() {
        super.onCreate();

        if(mGoogleApiClient == null){
            buildGoogleApiClient();
        }
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


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if(mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        LocationRequest request = LocationRequest.create().setInterval(100)
                .setFastestInterval(0)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setNumUpdates(1);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, request, this);

        if (lastLocation != null) {
            AppManager.getInstance().updateLocation(lastLocation);
            broadcastMessage(lastLocation);
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

    @Override
    public void onLocationChanged(Location location) {
        //update location
        AppManager.getInstance().updateLocation(location);
        broadcastMessage(location);
    }

    private void broadcastMessage(Location location) {
        Intent intent = new Intent(BROADCAST_INTENT);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCEL_LOCATION, location);
        intent.putExtra(BUNDLE_EXTRA, bundle);
        sendBroadcast(intent);
    }
}
