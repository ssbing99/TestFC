package my.com.taruc.fitnesscompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Food;
import my.com.taruc.fitnesscompanion.Classes.VirtualRacer;
import my.com.taruc.fitnesscompanion.UserLocalStore;

/**
 * Created by seebi_000 on 11/6/2016.
 */

public class FoodDetailDA {
    private UserLocalStore userLocalStore;
    private Context context;
    FitnessDB fitnessDB;
    SQLiteDatabase database;
int count =0;
    private  String TABLE_NAME = "Food_detail";
    private String columnID = "id";
    private String columnNutient = "nutient";
    private String columnAmount = "amount";
    private String columnUnit = "unit";
    private String columnFood = "FoodName";

    private String[] allColumn = {
            columnID ,  columnNutient, columnAmount,
            columnUnit, columnFood
    };
    public FoodDetailDA(Context context){
        this.context = context;
    }

    public ArrayList<Food> getAllDetail(String food) {

        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<Food> records = new ArrayList<Food>();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+columnFood+" = '"+food+"'";

       // Toast.makeText(context, "Food : "+food,Toast.LENGTH_SHORT).show();

        //try {
           // Cursor cursor = db.query(TABLE_NAME, allColumn, null, null, null, null, null);
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();
            if(cursor.getCount() ==0){
                Toast.makeText(context, "Current have no food found. ",Toast.LENGTH_SHORT).show();
            }else {
                while (!cursor.isAfterLast()) {
                    count++;
                    Food food1 = new Food();
                    food1.setId(cursor.getString(0));
                    food1.setNutient(cursor.getString(1));
                    food1.setAmount(cursor.getString(2));
                    food1.setUnit(cursor.getString(3));
                    food1.setName(cursor.getString(4));
                    records.add(food1);
                    cursor.moveToNext();
                }
                //Toast.makeText(context, "Record count: " + count, Toast.LENGTH_SHORT).show();
                cursor.close();
            }
        //} catch (SQLException e) {
        //    Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        //}

        return records;
    }
}
