package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

/**
 * Created by user on 19/10/2016.
 */

public class GPSManager{
    private static final int gpsMinTime = 500;
    private static final int gpsMinDistance = 0;

    private static LocationManager locManager = null;
    private static LocationListener locListener = null;
    private static GPSCallback gpsCallback = null;

    public GPSManager() {

        GPSManager.locListener = new LocationListener() {

            @Override
            public void onLocationChanged(final Location location) {
                if (GPSManager.gpsCallback != null) {
                    GPSManager.gpsCallback.onGPSUpdate(location);
                }
            }

            @Override
            public void onProviderDisabled(final String provider) {}

            @Override
            public void onProviderEnabled(final String provider) {}

            @Override
            public void onStatusChanged(final String provider, final int status, final Bundle extras) {}
        };
    }

    public GPSCallback getGPSCallback() {
        return GPSManager.gpsCallback;
    }

    public void setGPSCallback(final GPSCallback gpsCallback) {
        GPSManager.gpsCallback = gpsCallback;
    }

    public void startListening(final Context context) {
        if (GPSManager.locManager == null) {
            GPSManager.locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        final Criteria criteria = new Criteria();

        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setSpeedRequired(true);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        final String bestProvider = GPSManager.locManager.getBestProvider(criteria, true);

        if (bestProvider != null && bestProvider.length() > 0) {
            GPSManager.locManager.requestLocationUpdates(bestProvider, GPSManager.gpsMinTime,
                    GPSManager.gpsMinDistance, GPSManager.locListener);
        }
        else {
            final List<String> providers = GPSManager.locManager.getProviders(true);

            for (final String provider : providers) {
                GPSManager.locManager.requestLocationUpdates(provider, GPSManager.gpsMinTime,
                        GPSManager.gpsMinDistance, GPSManager.locListener);
            }
        }
    }

    public void stopListening() {
        try {
            if (GPSManager.locManager != null && GPSManager.locListener != null) {
                GPSManager.locManager.removeUpdates(GPSManager.locListener);
            }
            GPSManager.locManager = null;
        }
        catch (final Exception ex) {}
    }
}
