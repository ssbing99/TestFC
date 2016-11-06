package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import my.com.taruc.fitnesscompanion.Adapter.FoodAdapter;
import my.com.taruc.fitnesscompanion.Classes.Food;
import my.com.taruc.fitnesscompanion.R;
import my.com.taruc.fitnesscompanion.VirtualRacer.ExpandableListAdapter;

public class FoodPage extends ActionBarActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView listViewFood;
    List<String> foodCate;
    HashMap<String, List<String>> foodName;
    private ProgressDialog pDialog;
    TextView textViewTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_page);

        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTitle.setText("Food Category");

        pDialog = new ProgressDialog(this);

        listViewFood = (ExpandableListView) findViewById(R.id.listView);

        listViewFood.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                String name = foodName.get(foodCate.get(groupPosition)).get(childPosition).toString();

                Toast.makeText(getApplicationContext(), foodCate.get(groupPosition) + " : " + name, Toast.LENGTH_SHORT).show();
                loadFood(name);
/*
                Toast.makeText(getApplicationContext(), foodCate.get(groupPosition) + " : " + foodName.get(
                        foodCate.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
*/
                return false;
            }
        });

        try {
            // Check availability of network connection.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            Boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if (isConnected) {
                //new downloadCourse().execute(getResources().getString(R.string.get_course_url));
                if (!pDialog.isShowing()) {
                    pDialog.setMessage("Loading Data....");
                    pDialog.show();
                }
                downloadFood(this, "http://tarucfit.pe.hu/ServerRequest/FetchAllFood.php");
            } else {
                Toast.makeText(getApplication(), "Network is NOT available",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplication(),
                    "Error reading record:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        listAdapter = new ExpandableListAdapter(this, foodCate, foodName);
        listViewFood.setAdapter(listAdapter);

        listViewFood.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }

    private void downloadFood(Context context, String url) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        foodCate = new ArrayList<String>();
        foodName = new HashMap<String, List<String>>();

        foodCate.add("Cereal and cereal products");
        foodCate.add("Sugar and syrups");
        foodCate.add("Oils and fats");


                    JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                            new Response.Listener<JSONArray>() {
                                @Override
                    public void onResponse(JSONArray response) {


                        List<String> cereal = new ArrayList<String>();
                        List<String> sugar = new ArrayList<String>();
                        List<String> oil = new ArrayList<String>();

                        try {
                            //Clear list
                            //foodList.clear();
                            foodName.clear();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject courseResponse = (JSONObject) response.get(i);
                                String id = courseResponse.getString("id");
                                String name = courseResponse.getString("name");
                                String cate = courseResponse.getString("category");
                                Food food = new Food();
                                food.setId(id);
                                food.setName(name);
                                food.setCategory(cate);
                                //Toast.makeText(context,"Name "+food.getName(),Toast.LENGTH_SHORT).show();
                                //Toast.makeText(context,"Cate: "+food.getCategory(),Toast.LENGTH_LONG).show();

                                if (food.getCategory().equals(cate) && food.getCategory().contains("Cereals")) {
                                    cereal.add(food.getName());
                                }
                                    //Toast.makeText(context,"Fail 1: "+cate.length(),Toast.LENGTH_SHORT).show();

                                    if (food.getCategory().equals(cate) && food.getCategory().contains("Sugars")) {
                                        sugar.add(food.getName());
                                    }
                                        //Toast.makeText(context,"Fail 2: "+cate,Toast.LENGTH_SHORT).show();

                                        if (food.getCategory().equals(cate) && food.getCategory().contains("Oils")) {
                                            oil.add(food.getName());
                                        }
                                            //Toast.makeText(context,"Fail 3: "+cate,Toast.LENGTH_SHORT).show();

                                //foodList.add(food);
                            }

                            //loadFood();

                            foodName.put(foodCate.get(0), cereal);
                            foodName.put(foodCate.get(1), sugar);
                            foodName.put(foodCate.get(2), oil);

                            if (pDialog.isShowing())
                                pDialog.dismiss();

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error:" + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });
        queue.add(jsonObjectRequest);

    }

    private void loadFood(String name) {
        Intent intent = new Intent(this, FoodDetailPage.class);
        intent.putExtra("Name", name);
        startActivity(intent);
    }

    public void BackAction(View view) {
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}