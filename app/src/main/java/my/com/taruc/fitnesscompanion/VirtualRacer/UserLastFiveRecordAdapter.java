package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.VirtualRacer;
import my.com.taruc.fitnesscompanion.R;

/**
 * Created by user on 6/10/2016.
 */
/*
public class UserLastFiveRecordAdapter extends RecyclerView.Adapter<UserLastFiveRecordAdapter.MyAdapter> {
    private LayoutInflater inflater;
    private List<VirtualRacer> list = Collections.emptyList();
    Activity activity;
    Context context;


    public UserLastFiveRecordAdapter (Context context, List<VirtualRacer> list, Activity activity){
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = inflater.inflate(R.layout.user_lastfiverecord_layout, parent, false);
        MyAdapter viewHolder = new MyAdapter(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter holder, int position) {
        VirtualRacer vracer = list.get(position);
        holder.textViewDate.setText(vracer.getCreatedAt().toString());
        //textViewTime.setText(list.get(position).getTime());
        holder.textViewDistance.setText(String.valueOf(vracer.getDistance()));
        holder.textViewTimeUsed.setText(String.valueOf(vracer.getDuration()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyAdapter extends RecyclerView.ViewHolder{
        TextView textViewDate, textViewTime, textViewDistance, textViewTimeUsed;

        public MyAdapter (View view){
            super(view);
            textViewDate = (TextView) view.findViewById(R.id.textViewDate);
            textViewTime = (TextView) view.findViewById(R.id.textViewTime);
            textViewDistance = (TextView) view.findViewById(R.id.textViewDistance);
            textViewTimeUsed = (TextView) view.findViewById(R.id.textViewTimeUsed);
        }
    }
*/
public class UserLastFiveRecordAdapter extends ArrayAdapter {

    //private LayoutInflater inflater;
    List<VirtualRacer> list;
    Activity context;

    public UserLastFiveRecordAdapter (Activity context, List<VirtualRacer> l) {
        super(context, R.layout.user_lastfiverecord_layout, l);
        this.list = l;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewDate, textViewTime, textViewDistance, textViewTimeUsed;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.user_lastfiverecord_layout, parent, false);

        textViewDate = (TextView) rowView.findViewById(R.id.textViewDate);
        //textViewTime = (TextView) rowView.findViewById(R.id.textViewTime);
        textViewDistance = (TextView) rowView.findViewById(R.id.textViewDistance);
        textViewTimeUsed = (TextView) rowView.findViewById(R.id.textViewTimeUsed);

        textViewDate.setText(list.get(position).getCreatedAt().toString());
        //textViewTime.setText(list.get(position).getTime());
        textViewDistance.setText(Double.toString(list.get(position).getDistance()));
        textViewTimeUsed.setText(Double.toString(list.get(position).getDuration()));

        return rowView;
    }

}
