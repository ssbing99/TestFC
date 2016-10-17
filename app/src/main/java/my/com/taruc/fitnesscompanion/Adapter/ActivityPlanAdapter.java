package my.com.taruc.fitnesscompanion.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Database.ActivityPlanDA;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.UI.ActivityPlanPage;
import my.com.taruc.fitnesscompanion.UI.ExercisePage;

/**
 * Created by saiboon on 31/1/2016.
 */
public class ActivityPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<>();
    ArrayList<ActivityPlan> rearrangeActivityPlanArrayList = new ArrayList<>();
    ArrayList<Integer> headerPosition = new ArrayList<>();
    ArrayList<String> TypeValue = new ArrayList<>();
    ActivityPlanDA activityPlanDA;
    int tempPositionIndex = 0;
    private Activity activity;

    boolean noHeader = true;
    int index = 0;
    final int TYPE_HEADER = 0;
    final int TYPE_ITEM = 1;

    public ActivityPlanAdapter(Context context, Activity activity, ArrayList<ActivityPlan> activityPlanArrayList, ArrayList<String> TypeValue) {
        inflater = LayoutInflater.from(context);
        this.activity = activity;
        this.context = context;
        activityPlanDA = new ActivityPlanDA(context);
        this.activityPlanArrayList = activityPlanArrayList;
        this.TypeValue = TypeValue;

        //retrieve header position index
        int j = 0;
        for (int i = 0; i < activityPlanArrayList.size() + TypeValue.size(); i++) {
            if (i == 0) {
                //header
                rearrangeActivityPlanArrayList.add(null);
                headerPosition.add(tempPositionIndex);
                tempPositionIndex++;
            } else if (j > 0) {
                if ((!activityPlanArrayList.get(j).getType().equals(activityPlanArrayList.get(j - 1).getType())) && noHeader) {
                    //header
                    rearrangeActivityPlanArrayList.add(null);
                    headerPosition.add(tempPositionIndex);
                    tempPositionIndex++;
                    noHeader = false;
                } else {
                    rearrangeActivityPlanArrayList.add(activityPlanArrayList.get(j));
                    tempPositionIndex++;
                    j++;
                    noHeader = true;
                }
            } else {
                rearrangeActivityPlanArrayList.add(activityPlanArrayList.get(j));
                tempPositionIndex++;
                j++;
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        if (i == TYPE_HEADER) {
            View view = inflater.inflate(R.layout.adapter_activity_plan_header, parent, false);
            HeaderViewHolder holder = new HeaderViewHolder(view);
            return holder;
        } else {
            View view = inflater.inflate(R.layout.adapter_activity_plan_item, parent, false);
            ItemViewHolder holder = new ItemViewHolder(view);
            //holder.itemView.setOnClickListener(new PlanOnClickListener(i, index));
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder HeaderHolder = (HeaderViewHolder) holder;
            HeaderHolder.subTitle.setText(rearrangeActivityPlanArrayList.get(i+1).getType());
        } else {
            ItemViewHolder ItemHolder = (ItemViewHolder) holder;

            if (rearrangeActivityPlanArrayList.get(i).getType().equalsIgnoreCase("recommend")) {
                ItemHolder.smallIcon.setImageResource(R.drawable.icon_recommend);
            }else if(rearrangeActivityPlanArrayList.get(i).getActivityName().equalsIgnoreCase("running")){
                ItemHolder.smallIcon.setImageResource(R.drawable.ic_run);
            }else if(rearrangeActivityPlanArrayList.get(i).getActivityName().equalsIgnoreCase("cycling")){
                ItemHolder.smallIcon.setImageResource(R.drawable.ic_bike);
            }else if(rearrangeActivityPlanArrayList.get(i).getActivityName().equalsIgnoreCase("hiking")){
                ItemHolder.smallIcon.setImageResource(R.drawable.ic_hike);
            }else if(rearrangeActivityPlanArrayList.get(i).getActivityName().equalsIgnoreCase("workout")){
                ItemHolder.smallIcon.setImageResource(R.drawable.ic_exercise);
            }else if(rearrangeActivityPlanArrayList.get(i).getActivityName().equalsIgnoreCase("sport")){
                ItemHolder.smallIcon.setImageResource(R.drawable.ic_sport);
            }else{
                ItemHolder.smallIcon.setImageResource(R.drawable.ic_walk);
            }
            ItemHolder.detail.setText(rearrangeActivityPlanArrayList.get(i).getActivityName() + "\n"
                    + "Description: " + rearrangeActivityPlanArrayList.get(i).getDescription() + "\n"
                    + "Suggested Duration: " + rearrangeActivityPlanArrayList.get(i).getDuration() + "min\n"
                    + "Calories burn/min: " + rearrangeActivityPlanArrayList.get(i).getEstimateCalories() + "\n"
                    + "Maximum HR: " + rearrangeActivityPlanArrayList.get(i).getMaxHR() + "\n");
            //index++;
            holder.itemView.setOnClickListener(new PlanOnClickListener(i));
        }
    }

    @Override
    public int getItemCount() {
        return activityPlanArrayList.size() + TypeValue.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView subTitle;

        public HeaderViewHolder(View headerView) {
            super(headerView);
            subTitle = (TextView) headerView.findViewById(R.id.textViewTitleCaption);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView smallIcon;
        TextView detail;

        public ItemViewHolder(View itemView) {
            super(itemView);
            smallIcon = (ImageView) itemView.findViewById(R.id.smallIcon);
            detail = (TextView) itemView.findViewById(R.id.activityPlanDetail);
        }
    }

    private class PlanOnClickListener implements View.OnClickListener {
        private int position;

        PlanOnClickListener(int i) {
            position = i;
        }

        @Override
        public void onClick(View v) {
            if (!isHeader(position)) {
                ActivityPlanPage activityPlanPage = (ActivityPlanPage) activity;
                Bundle localBundle = new Bundle();
                localBundle.putString("ActivityPlanID", rearrangeActivityPlanArrayList.get(position).getActivityPlanID());
                Intent localIntent = new Intent(activityPlanPage, ExercisePage.class);
                localIntent.putExtras(localBundle);
                activityPlanPage.startActivity(localIntent);
            }
        }
    }

    public boolean isHeader(int position) {
        if (headerPosition.contains(position)) {
            return true;
        } else {
            return false;
        }
    }
}
