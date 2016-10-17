package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.animation.ObjectAnimator;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.BackgroundSensor.HeartRateSensor;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.CheckAchievement;
import my.com.taruc.fitnesscompanion.Classes.CountDownTimerWithPause;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Duration;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.HealthProfile;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.Database.FitnessRecordDA;
import my.com.taruc.fitnesscompanion.Database.HealthProfileDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Classes.Set;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmSound;
import my.com.taruc.fitnesscompanion.ServerAPI.RetrieveRequest;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.UI.ExercisePage;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class VirtualRacerMainActivity extends Activity implements View.OnClickListener {
    Context context;
    private FitnessRecordDA myFitnessRecordDA;
    private ActivityPlanDA myActivityPlanDA;
    private ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<>();
    private UserLocalStore userLocalStore;
    private ServerRequests serverRequests;
    private RetrieveRequest mRetreiveRequests;
    private FitnessRecord fitnessRecord;
    private ActivityPlan activityPlan;
    double totalHR = 0.0;
    static int HRno = 0;
    private CheckAchievement checkAchievement;
    boolean isStartedExerise = false;
    SharedPreferences sharedPreferences;

    //core data
    String activityPlanID;
    String fitnessRecordID;

    //Timer
    private AlarmSound alarmSound = new AlarmSound();
    private boolean timerRunning = false;

    //Challenge
    private boolean isChallenge = false;
    private FitnessRecord fitnessRecordFromServer;
    private CountDownTimerWithPause countDownTimer;
    private String challengeFitnessRecordID;
    private String challengeUserID;

    //HR sensor
    HeartRateSensor heartRateSensor;
    boolean HRAlertDialogExist = false;
    private BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private boolean denyBLE = false;
    BluetoothManager bluetoothManager;
    boolean alreadyOpenBluetooth = false;
    boolean HRStripExist = false;

    //Distance sensor
    MyLocationListener myLocationListener = new MyLocationListener();
    Intent intentDistance;
    boolean isChoice = false;
    Location location;
    protected LocationManager locationManager;
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 30000;
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    double plat, plon, clat, clon, dis, initial_dis=0, total_dis=0;
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;

    @BindView(R.id.btnSetTarget)
    ImageButton btnSetTarget;
    @BindView(R.id.btnSetChallenge)
    ImageButton btnSetChallenge;
    @BindView(R.id.btnViewPastRecord)
    ImageButton btnViewPastRecord;
    @BindView(R.id.distanceAmount)
    TextView distanceAmt;
    @BindView(R.id.durationAmount)
    TextView durationAmt;
    @BindView(R.id.CountDownTimer)
    TextView CountDownTimerText;
    @BindView(R.id.startButton)
    Button ViewStart;
    @BindView(R.id.chronometerTimer)
    Chronometer myChronometer;
    @BindView(R.id.TextViewStage)
    TextView TextViewStage;
    @BindView(R.id.currentDistanceResult)
    TextView txtDistance;

    ValueAnimator bg;
    AnimationDrawable animDraw;
    ImageView animImage;
    ImageView vr1,vr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_racer_main);
        ButterKnife.bind(this);

        btnSetTarget.setOnClickListener(this);
        btnSetChallenge.setOnClickListener(this);
        btnViewPastRecord.setOnClickListener(this);

        String dis,hour,min;
        dis = getIntent().getStringExtra("Distance");
        hour=getIntent().getStringExtra("Hour");
        min = getIntent().getStringExtra("Min");

        if(dis==null){
            dis="0";
        }
        if(hour == null){
            hour="0";
        }
        if(min == null || min.contains("")){
            min="00";
        }

            distanceAmt.setText(dis+" distances");
            durationAmt.setText(hour+"hour "+min+"mins");

        // Initialize Distance UI
        txtDistance.setText("--");

        ////////////////////////////////////////////////////////////////VR
        animImage = (ImageView)findViewById(R.id.stickman);
        animImage.setBackgroundResource(R.drawable.run);
        animDraw = (AnimationDrawable) animImage.getBackground();

        vr1 = (ImageView) findViewById(R.id.imageVR);
        vr2 = (ImageView) findViewById(R.id.imageVR2);

        //BACKGROUND
        bg = ObjectAnimator.ofFloat(1.0f,-0.1f);
        bg.setRepeatCount(ValueAnimator.INFINITE);
        bg.setInterpolator(new LinearInterpolator());
        bg.setDuration(10000L);

        bg.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                float width = vr1.getWidth();
                float translationX = width * progress;
                vr1.setTranslationX(translationX-width);
                vr2.setTranslationX(translationX-width);
            }
        });
    }

    @Override
    public void onClick(View v){
        Intent intent;
        switch(v.getId()){
            case R.id.btnSetTarget:
                intent = new Intent(this, SetTarget.class);
                startActivity(intent);
                break;
            case R.id.btnSetChallenge:
                intent = new Intent(this, SetChallenge.class);
                startActivity(intent);
                break;
            case R.id.btnViewPastRecord:
                intent = new Intent(this, ViewPastRecord.class);
                startActivity(intent);
                break;
        }

    }
    public void buttonStart(View view) {
        String txt = ViewStart.getText().toString();
        if (isChallenge) {
            if (txt.equalsIgnoreCase("Start")) {
                txtDistance.setText(String.format("%.2f", total_dis));
                startCountDownTimer();
                animDraw.start();
                bg.start();
                ViewStart.setText(R.string.stop);
                isStartedExerise = true;
            } else {
                stopCountDownTimer();
            }
        } else {
            TextViewStage.setVisibility(View.VISIBLE);
            if (txt.equalsIgnoreCase("Start")) {
                /*************Start Warm Up*************/
                TextViewStage.setText(R.string.warmingUp);
                resetChronometer();
                myChronometer.setOnChronometerTickListener(new TickListener(0, 5));
                startTimer();
                //txtDistance.setTextColor(Color.WHITE);
                ViewStart.setText(R.string.next);
            } else if (txt.equalsIgnoreCase("Next")) {
                /*************Start Exercise*************/
                TextViewStage.setText("Start " + activityPlan.getActivityName());
                resetChronometer();
                Duration myDuration = new Duration();
                myDuration.addMinutes(activityPlan.getDuration());
                myChronometer.setOnChronometerTickListener(new TickListener(myDuration.getHours(), myDuration.getMinutes()));
                startTimer();
                isStartedExerise = true;
                ViewStart.setText(R.string.stop);
            } else if (txt.equalsIgnoreCase("Stop")) {
                /*************Start Cool Down*************/
                stopExerciseTimer();

            } else {
                /*************End*************/
                TextViewStage.setText("End " + activityPlan.getActivityName());
                resetChronometer();
                ViewStart.setText(R.string.start);
                //txtDistance.setText(String.format("%.2f m", total_dis));
                txtDistance.setText("--");
            }
        }
    }
    //////////////////////////////////////COUNT DOWN

    public void startCountDownTimer() {
        countDownTimer = new CountDownTimerWithPause(fitnessRecordFromServer.getRecordDuration() * 1000, 1000, true) {

            public void onTick(long millisUntilFinished) {
                timerRunning = true;
                DateTime countingDown = new DateTime();
                countingDown.getTime().addSecond(millisUntilFinished / 1000);
                CountDownTimerText.setText(countingDown.getTime().getFullTimeString());
            }

            public void onFinish() {
                Log.i("CountDown timer", "Time up!");
                timerRunning = false;
                CountDownEnd();
            }
        }.create();

        //start count step
        //txtDistance.setTextColor(Color.WHITE);
    }

    public void stopCountDownTimer() {
        countDownTimer.pause();
        String message;
        if (challengeSuccess()) {
            message = "Your challenge is success! Are you sure wan to stop now?";
        } else {
            message = "Your challenge is almost done! Are you sure wan to stop now?";
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Stop Challenge")
                .setMessage(message)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        countDownTimer.cancel();
                        CountDownEnd();
                        ViewStart.setText(R.string.start);
                        isStartedExerise = false;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        countDownTimer.resume();
                    }
                }).create();
        dialog.show();
    }

    public void CountDownEnd() {
        String message;
        int resID;
        if (challengeSuccess()) {
            message = "You challenge successfully!";
            resID = R.raw.ta_da;
            serverRequests.gcmChallenge(challengeUserID);
        } else {
            message = "You challenge is fail!";
            resID = R.raw.smash_fail;
        }
        alarmSound.playRaw(this, resID , false);
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Time up!")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        alarmSound.stop();
                    }
                }).create();
        dialog.show();
        //reset Timer
        //*********************8addFitnessRecord();
        ViewStart.setText(R.string.start);
        isStartedExerise = false;
        DateTime startingTime = new DateTime();
        startingTime.getTime().addSecond(fitnessRecordFromServer.getRecordDuration());
        CountDownTimerText.setText(startingTime.getTime().getFullTimeString()); //set count down timer
        //reset distance
        total_dis = 0;

        //txtDistance.setTextColor(Color.GRAY);
    }

    public boolean challengeSuccess() {
        String[] distanceString = txtDistance.getText().toString().split("m");
        double userDistance = 0;
        if (!distanceString[0].trim().equals("--")) {
            userDistance = Double.parseDouble(distanceString[0]);
        }
        if (userDistance > fitnessRecordFromServer.getRecordDistance()) {
            return true;
        } else {
            return false;
        }
    }


    public void startTimer() {
        //start count time
        int stoppedMilliseconds = 0;
        timerRunning = true;
        String chronoText = myChronometer.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
        }
        myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        myChronometer.start();
    }

    public void pauseExerciseTimer() {
        myChronometer.stop();
        timerRunning = false;
    }

    public void stopExerciseTimer() {
        myChronometer.stop();
        timerRunning = false;
        String message = "Confirm stop fitness activity now?";
        if (getDuration() < 120 && activityPlan.getDuration() >= 2) {
            message = "If is more effective to do this exercise for at least 2 minutes. Are you sure you want to stop here?";
        }
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Stop activity")
                .setMessage(message)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //*********************addFitnessRecord();
                        //txtDistance.setTextColor(Color.GRAY);

                        isStartedExerise = false;
                        TextViewStage.setText(R.string.coolingDown);
                        resetChronometer();
                        myChronometer.setOnChronometerTickListener(new TickListener(0, 5));
                        startTimer();
                        ViewStart.setText(R.string.end);
                        total_dis = 0;
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        timerRunning = true;
                        myChronometer.start();
                    }
                }).create();
        dialog.show();
    }

    public void resetChronometer() {
        timerRunning = false;
        myChronometer.stop();
        int stoppedMilliseconds = 0;
        myChronometer.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
    }
    private class TickListener implements Chronometer.OnChronometerTickListener {
        private int hour;
        private int min;

        public TickListener(int hour, int min) {
            this.hour = hour;
            this.min = min;
        }

        @Override
        public void onChronometerTick(Chronometer chronometer) {
            int tickHours = 0;
            int tickMinutes = 0;
            int tickSeconds = 0;
            String chronoText = myChronometer.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 3) {
                tickHours = Integer.parseInt(array[0]);
                tickMinutes = Integer.parseInt(array[1]);
                tickSeconds = Integer.parseInt(array[2]);
            } else {
                tickMinutes = Integer.parseInt(array[0]);
                tickSeconds = Integer.parseInt(array[1]);
            }
            if (tickHours == hour && tickMinutes == min && tickSeconds == 00 && timerRunning) {
                timerRunning = true;
                alarmSound.play(context, 1);
            } else {
                if (alarmSound.isPlay()) {
                    alarmSound.stop();
                }
            }
        }
    }
    /*********************************************************************************************
     * Insert Fitness Record
     ********************************************************************************************/
/*
    public void addFitnessRecord() {
        try {
            String activityPlanID = getActivityPlanID();
            String currentDateTime = getCurrentDateTime();
            Double myDistance = getDistance();
            int myDuration = getDuration();
            double myCalories = getCalories(myDuration);
            double averageHeartRate = getAverageHeartRate();

            fitnessRecord = new FitnessRecord(myFitnessRecordDA.generateNewFitnessRecordID(),
                    userLocalStore.returnUserID() + "",
                    activityPlanID,
                    myDuration,
                    myDistance,
                    myCalories, 0,
                    averageHeartRate,
                    new DateTime(currentDateTime),
                    new DateTime().getCurrentDateTime());
            boolean success = myFitnessRecordDA.addFitnessRecord(fitnessRecord);
            if (success) {
                serverRequests.storeFitnessRecordInBackground(fitnessRecord);
                Toast.makeText(this, "Insert fitness record success", Toast.LENGTH_SHORT).show();
                checkAchievement.checkGoal();
            } else {
                Toast.makeText(this, "Insert fitness record fail", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
*/
    public String getCurrentDateTime() {
        //get current Datetime
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss.sss");
        String formattedTime = df.format(c.getTime());
        String formattedDate = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DATE);
        return formattedDate + " " + formattedTime;
    }

    public double getDistance() {
        //get Distance
        String[] distanceString = txtDistance.getText().toString().split("m");
        double distanceAmount = 0.0;
        if (!distanceString[0].trim().equals("--")) {
            distanceAmount = Double.parseDouble(distanceString[0].trim());
        }
        return distanceAmount;
    }

    public int getDuration() {
        int timerSecond;
        if (isChallenge) {
            DateTime RemainningTime = new DateTime();
            RemainningTime.setTime(CountDownTimerText.getText().toString());
            timerSecond = fitnessRecordFromServer.getRecordDuration() - (fitnessRecordFromServer.getRecordDuration() - RemainningTime.getTime().getTotalSeconds());

        } else {
            //get duration in second
            int stoppedMilliseconds = 0;
            String chronoText = myChronometer.getText().toString();
            String array[] = chronoText.split(":");
            if (array.length == 2) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000 + Integer.parseInt(array[1]) * 1000;
            } else if (array.length == 3) {
                stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000 + Integer.parseInt(array[1]) * 60 * 1000 + Integer.parseInt(array[2]) * 1000;
            }
            timerSecond = stoppedMilliseconds / 1000;
        }
        return timerSecond;
    }

    public double getCalories(int timerSecond) {
        //get calories
        // jogging general MET 7.0
        //url https://en.wikipedia.org/wiki/Metabolic_equivalent
        // Calories = METS x weight (kg) x time (hours) 
        //url http://www.mhhe.com/hper/physed/clw/webreview/web07/tsld007.htm
        HealthProfileDA healthProfileDA = new HealthProfileDA(this);
        HealthProfile healthProfile = healthProfileDA.getLastHealthProfile();
        double calories = 7.0 * healthProfile.getWeight() * (timerSecond / 60.0 / 60.0);
        return calories;
    }

    public double getAverageHeartRate() {
        //get HR
        double averageHR = 0.0;
        if (HRno != 0) {
            averageHR = totalHR / HRno;
        }
        return averageHR;
    }


    private class MyLocationListener implements LocationListener {

        public void onLocationChanged(Location location) {
            String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Log.i("ExercisePage-Location", message);
        }

        public void onStatusChanged(String s, int i, Bundle b) {
            Log.i("ExercisePage-Location", "Provider status changed");
        }

        public void onProviderDisabled(String s) {
            Log.i("ExercisePage-Location", "Provider disabled by the user. GPS turned off");
        }

        public void onProviderEnabled(String s) {
            Log.i("ExercisePage-Location", "Provider enabled by the user. GPS turned on");
        }

    }
}
