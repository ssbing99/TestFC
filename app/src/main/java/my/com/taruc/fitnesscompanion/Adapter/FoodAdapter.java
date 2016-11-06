package my.com.taruc.fitnesscompanion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.Food;
import my.com.taruc.fitnesscompanion.R;

public class FoodAdapter extends ArrayAdapter<Food> {

    List<Food> list;
    Activity context;

    public FoodAdapter(Activity context, List<Food> l) {
        super(context, R.layout.activity_food_adapter, l);
        this.list = l;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater  = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.activity_food_adapter, parent, false);

        TextView textViewN1, textViewA1, textViewU1;

        textViewN1 = (TextView)rowView.findViewById(R.id.textViewNutient1);
        textViewA1 = (TextView)rowView.findViewById(R.id.textViewAmount1);
        textViewU1 = (TextView)rowView.findViewById(R.id.textViewUnit1);


        textViewN1.setText(list.get(position).getNutient());
        textViewA1.setText(list.get(position).getAmount());
        textViewU1.setText(list.get(position).getUnit());
        return rowView;
    }
}
