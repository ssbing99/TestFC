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

        TextView textViewN1, textViewA1, textViewU1,textViewN2, textViewA2, textViewU2,textViewN3, textViewA3, textViewU3,
                textViewN4, textViewA4, textViewU4,textViewN5, textViewA5, textViewU5,textViewN6, textViewA6, textViewU6;

        textViewN1 = (TextView)rowView.findViewById(R.id.textViewNutient1);
        textViewA1 = (TextView)rowView.findViewById(R.id.textViewAmount1);
        textViewU1 = (TextView)rowView.findViewById(R.id.textViewUnit1);
        /*
        textViewN2 = (TextView)rowView.findViewById(R.id.textViewNutient2);
        textViewA2 = (TextView)rowView.findViewById(R.id.textViewAmount2);
        textViewU2 = (TextView)rowView.findViewById(R.id.textViewUnit2);
        textViewN3 = (TextView)rowView.findViewById(R.id.textViewNutient3);
        textViewA3 = (TextView)rowView.findViewById(R.id.textViewAmount3);
        textViewU3 = (TextView)rowView.findViewById(R.id.textViewUnit3);
        textViewN4 = (TextView)rowView.findViewById(R.id.textViewNutient4);
        textViewA4 = (TextView)rowView.findViewById(R.id.textViewAmount4);
        textViewU4 = (TextView)rowView.findViewById(R.id.textViewUnit4);
        textViewN5 = (TextView)rowView.findViewById(R.id.textViewNutient5);
        textViewA5 = (TextView)rowView.findViewById(R.id.textViewAmount5);
        textViewU5 = (TextView)rowView.findViewById(R.id.textViewUnit5);
        textViewN6 = (TextView)rowView.findViewById(R.id.textViewNutient6);
        textViewA6 = (TextView)rowView.findViewById(R.id.textViewAmount6);
        textViewU6 = (TextView)rowView.findViewById(R.id.textViewUnit6);
*/
        textViewN1.setText(list.get(position).getNutient());
        textViewA1.setText(list.get(position).getAmount());
        textViewU1.setText(list.get(position).getUnit());
        return rowView;
    }
}
