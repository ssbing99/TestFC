package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.com.taruc.fitnesscompanion.R;

public class SetChallenge extends Activity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String ,List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_challenge);

        //Get ListView
        expListView = (ExpandableListView)findViewById(R.id.lvExpSC);

        //Prepare the Data List
        prepareDataList();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        //Setting the List Adapter
        expListView.setAdapter(listAdapter);

        // Listview Group Click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

       /*
        // Listview Group Expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group Collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });
        */

        // Listview Child Click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                if(listDataHeader.get(groupPosition)== "Easy"){
                    if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "1 KM 1 Hour"){
                        challengeSet("1", "1", "0");
                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "2 KM 1 Hour 15 Minutes"){
                        challengeSet("2", "1", "15");
                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "3 KM 1 Hour 30 Minutes"){
                        challengeSet("3", "1", "30");
                    } else {
                        challengeSet("0", "0", "0");
                    }
                } else if (listDataHeader.get(groupPosition)== "Normal"){
                    if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "1 KM 30 Minutes"){
                        challengeSet("1", "0", "30");
                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "2 KM 1 Hour"){
                        challengeSet("2", "1", "0");
                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "3 KM 1 Hour 15 Minutes"){
                        challengeSet("3", "1", "15");
                    } else {
                        challengeSet("0", "0", "0");
                    }
                } else if (listDataHeader.get(groupPosition)== "Hard"){
                    if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "1 KM 15 Minutes"){
                        challengeSet("1", "0", "15");
                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "2 KM 30 Minutes"){
                        challengeSet("2", "0", "30");
                    } else if (listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition) == "3 KM 45 Minutes"){
                        challengeSet("3", "0", "45");
                    } else {
                        challengeSet("0", "0", "0");
                    }
                }


                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : " + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }

    private void prepareDataList(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        //Add Header data
        listDataHeader.add("Easy");
        listDataHeader.add("Normal");
        listDataHeader.add("Hard");

        //Add Child data
        List<String> Easy = new ArrayList<String>();
        Easy.add("1 KM 1 Hour");
        Easy.add("2 KM 1 Hour 15 Minutes");
        Easy.add("3 KM 1 Hour 30 Minutes");

        List<String> Normal = new ArrayList<String>();
        Normal.add("1 KM 30 Minutes");
        Normal.add("2 KM 1 Hour");
        Normal.add("3 KM 1 Hour 15 Minutes");

        List<String> Hard = new ArrayList<String>();
        Hard.add("1 KM 15 Minutes");
        Hard.add("2 KM 30 Minutes");
        Hard.add("3 KM 45 Minutes");

        listDataChild.put(listDataHeader.get(0), Easy);
        listDataChild.put(listDataHeader.get(1), Normal);
        listDataChild.put(listDataHeader.get(2), Hard);
    }

    public void challengeSet(String km, String hour, String min){
        Intent intent = new Intent(this, VirtualRacerMainActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("Distance", km);
        bundle.putString("Hour", hour);
        bundle.putString("Min", min);
        intent.putExtras(bundle);

        startActivity(intent);
        finish();
    }

}
