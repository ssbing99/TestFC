package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.Classes.Set;

public class SetTarget extends ActionBarActivity {
    EditText distance, hour, min;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_target);
    }

    public void targetSet(View view){
        Set setTarget = new Set();

        distance = (EditText)findViewById(R.id.speedEnter);
        hour = (EditText)findViewById(R.id.timeEnterHour);
        min = (EditText)findViewById(R.id.timeEnterMinute);
/*
        setTarget.setDistance(distance.getText().toString());
        setTarget.setHour(Integer.parseInt(hour.getText().toString()));
        setTarget.setMinute(Integer.parseInt(min.getText().toString()));
*/
        Intent intent = new Intent(this, VirtualRacerMainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("Distance",distance.getText().toString() );
        bundle.putString("Hour", hour.getText().toString());
        bundle.putString("Min", min.getText().toString());
        intent.putExtras(bundle);

        startActivity(intent);

    }

    public void targetReset(View view){

        distance = (EditText)findViewById(R.id.speedEnter);
        hour = (EditText)findViewById(R.id.timeEnterHour);
        min = (EditText)findViewById(R.id.timeEnterMinute);

        distance.setText("");
        hour.setText("");
        min.setText("");

    }
}
