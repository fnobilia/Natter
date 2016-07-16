package it.natter.utility;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

/**
 * Created by francesco on 23/03/14.
 */
public class LocationTracker extends Service implements LocationListener {

    private final Context mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    protected LocationManager locationManager;

    public LocationTracker(Context context) {
        this.mContext = context;
        getLocation();
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return longitude;
    }

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    public void showSettingsAlert(){
        //Hardware.showDialogToActiveGps(getApplicationContext());
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled){
                this.showSettingsAlert();
            }
            else{
                if(isNetworkEnabled){
                    this.canGetLocation = true;
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }

                }

                if (isGPSEnabled) {
                    this.canGetLocation = true;
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
