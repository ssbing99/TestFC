package my.com.taruc.fitnesscompanion.Database;

/**
 * Created by seebi_000 on 10/18/2016.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.VirtualRacer;
import my.com.taruc.fitnesscompanion.UserLocalStore;

public class VRRecordDA {
    private UserLocalStore userLocalStore;
    private Context context;
    FitnessDB fitnessDB;
    SQLiteDatabase database;
int count =0;
    private  String TABLE_NAME = "VR_Record";
    private String columnID = "id";
    private String columnUserID = "user_id";
    private String columnDuration = "duration";
    private String columnDistance = "distance";
    private String columnSpeed = "speed";
    private String columnCreatedAt = "created_at";
    private String columnUpdatedAt = "updated_at";
    private String[] allColumn = {
            columnID ,  columnUserID, columnDuration,
            columnDistance, columnSpeed, columnCreatedAt,  columnUpdatedAt
};

    public VRRecordDA(Context context){
        this.context = context;
    }

    public boolean insertRecord(VirtualRacer vracer){
        ContentValues values = new ContentValues();
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        DateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        long date = System.currentTimeMillis();
        boolean success = false;
        try {
            values.put(columnID, vracer.getId());
            values.put(columnUserID, vracer.getUserID());
            values.put(columnDuration, vracer.getDuration());
            values.put(columnDistance, vracer.getDistance());
            values.put(columnSpeed, vracer.getSpeed());
            values.put(columnCreatedAt, vracer.getCreatedAt().toString());//format.format(date)
            values.put(columnUpdatedAt, vracer.getUpdatedAt().toString());
            db.insert(TABLE_NAME, null, values);
            success = true;
            Toast.makeText(context,"Inserting to DB", Toast.LENGTH_SHORT).show();
        }catch (SQLException e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return success;
    }

    public ArrayList<VirtualRacer> getAllVRRecord(String userId) {

        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        ArrayList<VirtualRacer> records = new ArrayList<VirtualRacer>();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+columnUserID+" ='"+userId+"'";
        //String sql = "SELECT * FROM "
        Toast.makeText(context, "User : "+userId,Toast.LENGTH_SHORT).show();

            try {
                //Cursor cursor = db.query(TABLE_NAME, allColumn, null, null, null, null, null);
                Cursor cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                if(cursor.getCount() ==0){
                    Toast.makeText(context, "Current have no record not found. ",Toast.LENGTH_SHORT).show();
                }else {


                    while (!cursor.isAfterLast()) {
                        count++;
                        VirtualRacer vrRecord = new VirtualRacer();
                        vrRecord.setId(cursor.getString(0));
                        vrRecord.setUserID(cursor.getString(1));
                        vrRecord.setDuration(Double.parseDouble(cursor.getString(2)));
                        vrRecord.setDistance(Double.parseDouble(cursor.getString(3)));
                        vrRecord.setSpeed(Integer.parseInt(cursor.getString(4)));
                        vrRecord.setCreatedAt(cursor.getString(5));
                        vrRecord.setUpdatedAt(cursor.getString(6));
                        records.add(vrRecord);
                        cursor.moveToNext();
                    }
                    Toast.makeText(context, "Record count: " + count, Toast.LENGTH_SHORT).show();
                    cursor.close();
                }
            } catch (SQLException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

        return records;
    }

    ////////////////////////////////////////////////////


    public String generateNewVRRecordID(String userId){
        String newVRRecordID="";
        VirtualRacer lastVRRecord;
        int checkCount=0;
        Calendar c = Calendar.getInstance();
        String myDate = c.get(Calendar.DATE) + "/"+ (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR);
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
        Date dateObj = new Date();
        try {
            dateObj = curFormater.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = df.format(dateObj); //current date

        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE "+columnUserID+" ='"+userId+"'";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            //getAllVRRecord(userId);
            count=cursor.getCount();
            count++;
            newVRRecordID = Integer.toString(count);
            Toast.makeText(context,"ID is : "+count,Toast.LENGTH_SHORT).show();
            //count=0;
        }catch (Exception ex){
            //newFitnessRecordID = formattedDate + "FR001" ;
            Toast.makeText(context, ex.toString(),Toast.LENGTH_SHORT).show();
        }
        return newVRRecordID;
    }
}
