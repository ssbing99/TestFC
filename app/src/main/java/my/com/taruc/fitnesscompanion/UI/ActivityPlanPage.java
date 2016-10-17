package my.com.taruc.fitnesscompanion.UI;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.com.taruc.fitnesscompanion.Adapter.ActivityPlanAdapter;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.R;

public class ActivityPlanPage extends Activity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.RecycleViewCommonActivityPlan)
    RecyclerView RecycleViewCommonActivityPlan;

    ActivityPlanAdapter activityPlanAdapter;
    ActivityPlanDA myActivityPlanDA;
    ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<>();
    ArrayList<ActivityPlan> activityPlanArrayListTemp = new ArrayList<>();
    ArrayList<String> activityTypeArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_plan_page);
        ButterKnife.bind(this);

        textViewTitle.setText(R.string.activityPlan);

        // get activity plan
        myActivityPlanDA = new ActivityPlanDA(this);
        activityPlanArrayList = myActivityPlanDA.getAllActivityPlan();
        activityTypeArrayList = myActivityPlanDA.getAllActivityPlanType();
        RecycleViewCommonActivityPlan.setLayoutManager(new LinearLayoutManager(this));
        activityPlanAdapter = new ActivityPlanAdapter(this, this, activityPlanArrayList, activityTypeArrayList);
        RecycleViewCommonActivityPlan.setAdapter(activityPlanAdapter);
    }

    public void BackAction(View view) {
        this.finish();
    }

}
