package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.VirtualRacer;
import my.com.taruc.fitnesscompanion.Database.VRRecordDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.VirtualRacer.UserLastFiveRecordAdapter;

public class ViewPastRecord extends ActionBarActivity {
    RecyclerView recyclerView;
    private VRRecordDA vrRecordDA;
    UserLastFiveRecordAdapter listAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_past_record);

        vrRecordDA = new VRRecordDA(this);
//        vrRecordDA.open();
        final List<VirtualRacer> vrRecordList = vrRecordDA.getAllVRRecord();
        recyclerView = (RecyclerView)findViewById(R.id.lvLast5Record);
        listAdapter = new UserLastFiveRecordAdapter(getApplicationContext(), vrRecordList,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listAdapter);
        /*

            listView = (ListView) findViewById(R.id.lvLast5Record);
            listView.setAdapter(listAdapter);
        */
        //    Toast.makeText(this,"Is empty",Toast.LENGTH_SHORT).show();


    }


/*
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_view_past_record, container, false);
        vrRecordDA = new VRRecordDA(getActivity().getApplicationContext());
        vrRecordDA.open();
        final List<VirtualRacer> vrRecordList = vrRecordDA.getAllVRRecord();

        UserLastFiveRecordAdapter listAdapter = new UserLastFiveRecordAdapter(getActivity(), vrRecordList);

        listView = (ListView)view.findViewById(R.id.lvLast5Record);
        listView.setAdapter(listAdapter);

        return view;
    }
    */

    public void BackAction(View view) {
        this.finish();
    }
}
