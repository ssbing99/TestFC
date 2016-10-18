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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.VirtualRacer;

public class VRRecordDA {
    private Context context;
    FitnessDB fitnessDB;
    SQLiteDatabase database;

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

    public void open() throws SQLException {
        //database = dbHelper.getWritableDatabase();
    }

    public List<VirtualRacer> getAllVRRecord() {
        fitnessDB = new FitnessDB(context);
        SQLiteDatabase db = fitnessDB.getWritableDatabase();
        List<VirtualRacer> records = new ArrayList<VirtualRacer>();

        //String sql = "SELECT * FROM "

            try {
                Cursor cursor = db.query(TABLE_NAME, allColumn, null, null, null, null, null);
                cursor.moveToFirst();

                while (!cursor.isAfterLast()) {
                    VirtualRacer vrRecord = new VirtualRacer();
                    vrRecord.setId(cursor.getString(0));
                    vrRecord.setUserID(cursor.getString(1));
                    vrRecord.setDuration(Integer.parseInt(cursor.getString(2)));
                    vrRecord.setDistance(Integer.parseInt(cursor.getString(3)));
                    vrRecord.setSpeed(Integer.parseInt(cursor.getString(4)));
                    vrRecord.setCreatedAt(new DateTime(cursor.getString(5)));
                    vrRecord.setUpdatedAt(new DateTime(cursor.getString(6)));
                    records.add(vrRecord);
                    cursor.moveToNext();
                }
                cursor.close();
            } catch (SQLException e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

        return records;
    }
}
