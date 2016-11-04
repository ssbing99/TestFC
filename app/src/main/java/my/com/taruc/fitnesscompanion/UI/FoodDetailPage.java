package my.com.taruc.fitnesscompanion.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;
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

public class FoodDetailPage extends ActionBarActivity {
    ListView listViewDetail;
    List<Food> foodDetail;
    String name;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail_page);

        listViewDetail = (ListView) findViewById(R.id.listView);
        pDialog = new ProgressDialog(this);
        foodDetail = new ArrayList<>();

        name = getIntent().getStringExtra("Name");

        try {
            // Check availability of network connection.
            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            Boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
            if (isConnected) {
                //new downloadCourse().execute(getResources().getString(R.string.get_course_url));
                //
                String url =  "http://tarucfit.pe.hu/ServerRequest/FetchFoodDetail.php?name="+name;
                downloadFoodDetail(this, url);
                //downloadFoodDetail(this, "http://tarucfit.pe.hu/ServerRequest/FetchAllFood.php");
                //Toast.makeText(this,"URL: " +url,Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplication(), "Network is NOT available",
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplication(),
                    "Error reading record:" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

/*
        final FoodAdapter adapter = new FoodAdapter(this, foodList);
        listViewFood.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Count :" + foodList.size(), Toast.LENGTH_SHORT).show();
        */


    }
    private void downloadFoodDetail(Context context, String url) {
        //mPostCommentResponse.requestStarted();
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (!pDialog.isShowing()) {
                            pDialog.show();
                        }
                        try{
                            //Clear list
                            foodDetail.clear();

                            for(int i=0; i < response.length();i++){
                                JSONObject courseResponse = (JSONObject) response.get(i);
                                String nutient = courseResponse.getString("nutient");
                                String amt = courseResponse.getString("amount");
                                String unit = courseResponse.getString("unit");
                                Food food = new Food();
                                food.setNutient(nutient);
                                food.setAmount(amt);
                                food.setUnit(unit);
                                foodDetail.add(food);
                            }
                            loadDetail();

                            if (pDialog.isShowing())
                                pDialog.dismiss();

                        }catch (Exception e){
                           Toast.makeText(getApplicationContext(), "Error:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                       }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), "Error:" + volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void loadDetail() {
        final FoodAdapter adapter = new FoodAdapter(this, foodDetail);
        listViewDetail.setAdapter(adapter);
        Toast.makeText(getApplicationContext(), "Count :" + foodDetail.size(), Toast.LENGTH_SHORT).show();
    }


}
