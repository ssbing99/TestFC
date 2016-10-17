package my.com.taruc.fitnesscompanion.Reminder;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UI.ReminderNewPage;

/**
 * Created by saiboon on 12/7/2015.
 */
public class AdapterReminderNew extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    AdapterReminderNewListValue tempValues=null;
    int i=0;

    /*************  AdapterReminderNew Constructor *****************/
    public AdapterReminderNew(Activity a, ArrayList d, Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {
        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView text;
        public TextView text1;
        public TextView textWide;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.schedule_new_tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.text01);
            holder.text1=(TextView)vi.findViewById(R.id.text02);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setText("No Data");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = (AdapterReminderNewListValue) data.get( position );
            AdapterReminderNewListValue tempValuesPrevious = new AdapterReminderNewListValue();
            if (position>0) {
                tempValuesPrevious = (AdapterReminderNewListValue) data.get(position - 1);
            }

            /************  Set Model values in Holder elements ***********/

            holder.text.setText( tempValues.getTitle() );
            holder.text1.setText(tempValues.getChoice());

            if (tempValues.getTitle().equals("Day")){
                if (!tempValuesPrevious.getChoice().equals("Weekly")){
                    holder.text.setTextColor(activity.getResources().getColor(R.color.DisableColor));
                    holder.text1.setTextColor(activity.getResources().getColor(R.color.DisableColor));
                }else{
                    holder.text.setTextColor(activity.getResources().getColor(R.color.FontColor));
                    holder.text1.setTextColor(activity.getResources().getColor(R.color.FontColor));
                }
            }

            /******** Set Item Click Listner for LayoutInflater for each row *******/
            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("AdapterReminderNew", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;
        OnItemClickListener(int position){
            mPosition = position;
        }
        @Override
        public void onClick(View arg0) {
            ReminderNewPage sct = (ReminderNewPage) activity;
            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
            sct.onItemClick(mPosition);
        }
    }



}