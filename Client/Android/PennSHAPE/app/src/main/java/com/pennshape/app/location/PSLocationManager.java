package com.pennshape.app.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.pennshape.app.model.PSDataStore;
import com.pennshape.app.request.PSHttpTaskRequest;
import com.pennshape.app.request.PSLocationUploadTaskRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by hasun on 11/11/15.
 */
public class PSLocationManager implements LocationListener, PSHttpTaskRequest.PSHttpTaskRequestHandler{
    private static PSLocationManager sharedInstance = new PSLocationManager();
    private Context mContext;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 100; // 100 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 5; // 5 minute
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    Location location; // location

    public static PSLocationManager sharedManager() {
        return sharedInstance;
    }

    public void setContext(Context context){
        mContext = context;
    }

    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    public void fireLocation() {
        getLocation();
        if(location!=null) {
            PSLocationUploadTaskRequest request = new PSLocationUploadTaskRequest();
            request.setUserID(PSDataStore.getInstance().getUserID());
            request.setLatitude(location.getLatitude());
            request.setLongitude(location.getLongitude());
            request.setTime(dateFormat.format(Calendar.getInstance().getTime()));
            request.handler = this;
            request.execute();
        }
    }

    private void showLocation() {
        if(location!=null) {
            Toast.makeText(mContext, "" + location.getLatitude() + " - " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onSuccess(PSHttpTaskRequest request, Object result) {
        if(request instanceof PSLocationUploadTaskRequest){
            showLocation();
        }
    }

    @Override
    public void onFailure(PSHttpTaskRequest request, String error) {

    }
}
