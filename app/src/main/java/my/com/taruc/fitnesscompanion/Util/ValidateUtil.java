package my.com.taruc.fitnesscompanion.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;

/**
 * Created by Hexa-Jackson on 1/6/2016.
 */
public class ValidateUtil {

    public ValidateUtil() {
    }

    public static boolean isEmail(String email) {
        final Pattern rfc2822 = Pattern.compile("^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$");
        return rfc2822.matcher(email).matches();
    }

    public static boolean isEmpty(List<?> list) {
        if (list == null || list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(Object[] object) {
        if (object == null || object.length == 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * Returns true if value is null or has the value "null".
     *
     * @param value             The value.
     * @return  true or false.
     */
    public static boolean isNullString(String value) {
        if (value == null) {
            return true;
        }
        if ("null".equals(value)) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if value is null or has the value "null"
     * or empty string "". Will also trim the value.
     *
     * @param value             The value.
     * @return  true or false.
     */
    public static boolean isNullOrEmptyString(String value) {
        if (value == null) {
            return true;
        }
        if ("null".equalsIgnoreCase(value)) {
            return true;
        }
        value = value.trim();
        if (value.length() <= 0) {
            return true;
        }
        return false;
    }


    public static boolean isNumeric(String value) {
        if (value == null) {
            return true;
        }
        if ("null".equalsIgnoreCase(value)) {
            return true;
        }
        value = value.trim();
        if (value.length() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isArrayListSame (ArrayList<ActivityPlan> oldList, ArrayList<ActivityPlan> newList){
        for (int i = 0 ; i < oldList.size(); i ++){
            if(!oldList.get(i).getActivityName().equals(newList.get(i).getActivityName())){
                return false;
            }
        }
        return true;
    }

    public static boolean checkPlayServices(Context mContext) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(mContext);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog((Activity)mContext, resultCode, Constant.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

            }
            return false;
        }
        return true;
    }

    public static boolean isMyServiceRunning(Context mContext, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
