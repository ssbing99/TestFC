package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.provider.BaseColumns;

/**
 * Created by user on 6/10/2016.
 */

public final class UserContract {

    public UserContract(){}

    public static abstract class User implements BaseColumns {
        public static final String TABLE_NAME = "pastrecord";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DISTANCE = "distance";
        public static final String COLUMN_TIMEUSED = "timeused";
    }

}
