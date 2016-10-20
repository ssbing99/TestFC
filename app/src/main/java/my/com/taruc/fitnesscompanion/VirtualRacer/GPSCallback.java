package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.location.Location;

/**
 * Created by user on 19/10/2016.
 */

public interface GPSCallback {
    public abstract void onGPSUpdate(Location location);
}
