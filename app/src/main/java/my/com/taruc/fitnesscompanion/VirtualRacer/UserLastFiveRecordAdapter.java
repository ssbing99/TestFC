package my.com.taruc.fitnesscompanion.VirtualRacer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import my.com.taruc.fitnesscompanion.R;

/**
 * Created by user on 6/10/2016.
 */

public class UserLastFiveRecordAdapter extends ArrayAdapter {

    List<UserLastFiveRecord> list;
    Activity context;

    public UserLastFiveRecordAdapter (Activity context, List<UserLastFiveRecord> l) {
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
        textViewTime = (TextView) rowView.findViewById(R.id.textViewTime);
        textViewDistance = (TextView) rowView.findViewById(R.id.textViewDistance);
        textViewTimeUsed = (TextView) rowView.findViewById(R.id.textViewTimeUsed);

        textViewDate.setText(list.get(position).getDate());
        textViewTime.setText(list.get(position).getTime());
        textViewDistance.setText(list.get(position).getDistance());
        textViewTimeUsed.setText(list.get(position).getTimeused());

        return rowView;
    }
}
