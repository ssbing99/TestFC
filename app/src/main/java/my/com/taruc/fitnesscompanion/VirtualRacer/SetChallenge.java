package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
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

        // Listview Child Click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
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
        listDataHeader.add("Medium");
        listDataHeader.add("Hard");

        //Add Child data
        List<String> Easy = new ArrayList<String>();
        Easy.add("Easy 1");
        Easy.add("Easy 2");
        Easy.add("Easy 3");

        List<String> Normal = new ArrayList<String>();
        Normal.add("Normal 1");
        Normal.add("Normal 2");
        Normal.add("Normal 3");

        List<String> Hard = new ArrayList<String>();
        Hard.add("Hard 1");
        Hard.add("Hard 2");
        Hard.add("Hard 3");

        listDataChild.put(listDataHeader.get(0), Easy);
        listDataChild.put(listDataHeader.get(1), Normal);
        listDataChild.put(listDataHeader.get(2), Hard);
    }
}
