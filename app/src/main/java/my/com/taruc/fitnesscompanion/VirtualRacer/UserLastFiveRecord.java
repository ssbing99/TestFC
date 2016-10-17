package my.com.taruc.fitnesscompanion.VirtualRacer;

/**
 * Created by user on 6/10/2016.
 */

import my.com.taruc.fitnesscompanion.VirtualRacer.UserContract.User;

public class UserLastFiveRecord {
    private String date;
    private String time;
    private String distance;
    private String timeused;

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getDistance() {return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getTimeused() { return timeused; }

    public void setTimeused(String timeused) { this.timeused = timeused; }

    @Override
    public String toString() {
        return User.COLUMN_DATE + ":" + this.date +
                "," + User.COLUMN_TIME + ":" + this.time +
                "," + User.COLUMN_DISTANCE + ":" + this.distance +
                "," + User.COLUMN_TIMEUSED + ":" + this.timeused;
    }
}
