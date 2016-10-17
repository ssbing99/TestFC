package my.com.taruc.fitnesscompanion;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.UserProfile;
import my.com.taruc.fitnesscompanion.GCM.QuickstartPreferences;
import my.com.taruc.fitnesscompanion.GCM.RegistrationIntentService;
import my.com.taruc.fitnesscompanion.ServerAPI.GetUserCallBack;
import my.com.taruc.fitnesscompanion.ServerAPI.RetrieveRequest;
import my.com.taruc.fitnesscompanion.ServerAPI.ServerRequests;
import my.com.taruc.fitnesscompanion.Util.ValidateUtil;

public class SignUpPage extends FragmentActivity implements View.OnClickListener {

    public static final String TAG = SignUpPage.class.getName();
    private static final int INITIAL_REWARD = 0;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    private String DOJ;
    private ServerRequests serverRequests;

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etDOB)
    EditText etDOB;
    @BindView(R.id.radioButtonMale)
    RadioButton rbMale;
    @BindView(R.id.radioButtonFemale)
    RadioButton rbFemale;
    @BindView(R.id.etHeight)
    EditText etHeight;
    @BindView(R.id.etWeight)
    EditText etWeight;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.etPasswordConfirmation)
    EditText etPasswordConfirmation;

    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyy-MM-dd");
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    // Connection detector class
    private ConnectionDetector cd;
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Alert Dialog Manager
    ShowAlert alert = new ShowAlert();

    private String mEmail;
    private String mName;
    private String mDOB;
    private String mGender;
    private Double mHeight;
    private Double mWeight;
    private String mPassword;

    public String result;
    private SlideDateTimeListener listener;
    private RetrieveRequest mRetrieveRequest;
    private boolean mIsEmailExist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        ButterKnife.bind(this);

        textViewTitle.setText("Sign Up");
        btnRegister.setOnClickListener(this);
        etDOB.setOnClickListener(this);
        serverRequests = new ServerRequests(this);
        mRetrieveRequest = new RetrieveRequest(this);
        cd = new ConnectionDetector(this);
        listener = new SlideDateTimeListener() {
            @Override
            public void onDateTimeSet(Date date) {
                etDOB.setText(mFormatter.format(date));
            }

            // Optional cancel listener
            @Override
            public void onDateTimeCancel() {
//            Toast.makeText(SignUpPage.this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        };

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                result = intent.getStringExtra("GCM");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
//                    GCM.setText(result);
                } else {
//                    GCM.setText(getString(R.string.token_error_message));
                }
            }
        };

        if (ValidateUtil.checkPlayServices(this)) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sign_up_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                isInternetPresent = cd.haveNetworkConnection();
                if (isInternetPresent) {
                    if (etEmail.getText().toString().isEmpty()) {
                        showErrorMessage("Email Field Cant Leave it Blank.Please Check and Try Again!");
                    } else if (ValidateUtil.isEmpty(etName.getText().toString())) {
                        showErrorMessage("Name Cant Leave it Blank.Please Check and Try Again!");
                    } else if (etDOB.getText().toString().isEmpty()) {
                        showErrorMessage("Date of Birth Cant Leave it Blank.Please Check and Try Again!");
                    } else if (ValidateUtil.isEmpty(etHeight.getText().toString())) {
                        showErrorMessage("Height Field Cant Leave it Blank.Please Check and Try Again!");
                    } else if (ValidateUtil.isEmpty(etWeight.getText().toString())) {
                        showErrorMessage("Weight Field Cant Leave it Blank.Please Check and Try Again!");
                    } else if (etPassword.getText().toString().isEmpty()) {
                        showErrorMessage("Password Field Cant Leave it Blank.Please Check and Try Again!");
                    } else if (etPasswordConfirmation.getText().toString().isEmpty()) {
                        showErrorMessage("Confirmation Password Field Cant Leave it Blank.Please Check and Try Again!");
                    } else {
                        if (rbMale.isChecked()) {
                            mGender = rbMale.getText().toString();
                        } else if (rbFemale.isChecked()) {
                            mGender = rbFemale.getText().toString();
                        }
                        mEmail = etEmail.getText().toString();
                        mIsEmailExist = checkEmailExist(mEmail);
                        mName = etName.getText().toString();
                        mDOB = etDOB.getText().toString();
                        mHeight = Double.valueOf(etHeight.getText().toString());
                        mWeight = Double.valueOf(etWeight.getText().toString());
                        mPassword = etPassword.getText().toString();
                        int confirm = mPassword.compareTo(etPasswordConfirmation.getText().toString());
                        Boolean emailTrue = ValidateUtil.isEmailValid(mEmail);
                        if (!emailTrue) {
                            showErrorMessage("Email Address Format Not Correct.Please Check and Try Again");
                        } else if (mIsEmailExist) {
                            showErrorMessage("Email Address was existed in our Server. Please Retry");
                        } else if (confirm != 0) {
                            showErrorMessage("Password Mismatch. Please Retry");
                        } else {
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            DOJ = df.format(c.getTime());
                            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.user_profile_grey);
                            Integer countID = serverRequests.returnCountID();
                            UserProfile userProfile = new UserProfile(countID.toString(), result, mEmail, mPassword, mName, new DateTime(mDOB), mGender, mWeight, mHeight, INITIAL_REWARD, new DateTime(DOJ), new DateTime(DOJ), bitmap);
                            registerUser(userProfile);
                        }
                    }
                } else {
                    alert.showAlertDialog(this, "Internet Connection Error", "Please Check Your Internet Connection", false);
                }
                break;
            case R.id.etDOB:
                new SlideDateTimePicker.Builder(getSupportFragmentManager()).setListener(listener).setInitialDate(new Date())
                        //.setMinDate(minDate)
                        .setMaxDate(new Date())
                                //.setIs24HourTime(true)
                        .setTheme(SlideDateTimePicker.HOLO_DARK)
                                //.setIndicatorColor(Color.parseColor("#990000"))
                        .build().show();
                break;
        }
    }

    private void registerUser(UserProfile userProfile) {
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(userProfile, new GetUserCallBack() {
            @Override
            public void done(UserProfile returnUserProfile) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpPage.this);
                dialogBuilder.setTitle("Success");
                dialogBuilder.setMessage("Register Successfully! Please Login to enjoy more features");
                dialogBuilder.setPositiveButton("Proceed To Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SignUpPage.this, LoginPage.class));
                        finish();
                    }
                });
                dialogBuilder.show();
            }
        });
    }


    private boolean checkEmailExist(String userEmail) {
        ArrayList<String> email = mRetrieveRequest.fetchAllEmailInBackground();
        for (int i = 0; i < email.size(); i++) {
            if (userEmail.equals(email.get(i))) {
                return true;
            }
        }
        return false;
    }


    private void showErrorMessage(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignUpPage.this);
        dialogBuilder.setTitle("Error Message");
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    public void BackAction(View view) {
        this.finish();
    }

}