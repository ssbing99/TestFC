package my.com.taruc.fitnesscompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import my.com.taruc.fitnesscompanion.BackgroundSensor.AccelerometerSensor2;
import my.com.taruc.fitnesscompanion.BackgroundSensor.TheService;
import my.com.taruc.fitnesscompanion.HRStripBLE.DeviceScanActivity;
import my.com.taruc.fitnesscompanion.HeartRateCamera.HeartRateMonitor;
import my.com.taruc.fitnesscompanion.Reminder.AlarmService.AlarmServiceController;
import my.com.taruc.fitnesscompanion.UI.IChoiceActivity;
import my.com.taruc.fitnesscompanion.UI.MainMenu;
import my.com.taruc.fitnesscompanion.VirtualRacer.VirtualRacerMainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {

    public static final String PREF_FILE_NAME = "testpref";
    public static final String KEY_USER_LEARNED_DRAWER = "user_learner_drawer";

    @BindView(R.id.logoDrawer)
    ImageView logoDrawer;
    @BindView(R.id.btnPairIChoice)
    Button btnPairIChoice;
    @BindView(R.id.btnPairHR)
    Button btnPairHR;
    @BindView(R.id.btnVirtualRacer)
    Button btnVirtualRacer;
    @BindView(R.id.btnCheckHR)
    Button btnCheckHR;
    @BindView(R.id.btnSignOut)
    Button btnSignOut;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View view_container;
    private AlarmServiceController alarmServiceController;
    UserLocalStore userLocalStore;

    private Unbinder unbinder;

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        userLocalStore = new UserLocalStore(view.getContext());
        FacebookSdk.sdkInitialize(view.getContext());
        ButterKnife.bind(this, view);
        btnPairIChoice.setOnClickListener(this);
        btnPairHR.setOnClickListener(this);
        btnVirtualRacer.setOnClickListener(this);
        btnCheckHR.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        alarmServiceController = new AlarmServiceController(view.getContext());
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    public void setUp(int fragmentId, final DrawerLayout drawerLayout, final Toolbar toolbar) {
        view_container = getActivity().findViewById(fragmentId);
        mDrawLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };

        if (!mUserLearnedDrawer) {
            mDrawLayout.openDrawer(view_container);
        }

        mDrawLayout.setDrawerListener(mDrawerToggle);
        mDrawLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnPairIChoice:
                intent = new Intent(getActivity().getApplicationContext(), IChoiceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btnPairHR:
                intent = new Intent(getActivity().getApplicationContext(), DeviceScanActivity.class);
                startActivity(intent);
                break;
            case R.id.btnVirtualRacer:
                intent = new Intent(getActivity().getApplicationContext(), VirtualRacerMainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCheckHR:
                intent = new Intent(getActivity().getApplicationContext(), HeartRateMonitor.class);
                startActivity(intent);
                break;
            case R.id.btnSignOut:
                userLocalStore.clearUserData();
                getActivity().stopService(new Intent(getContext(), TheService.class));
                getActivity().stopService(new Intent(getContext(), AccelerometerSensor2.class));
                alarmServiceController.deactivateReminders();
                LoginManager.getInstance().logOut();
                intent = new Intent(getActivity().getApplicationContext(), LoginPage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();
        }
    }
}
