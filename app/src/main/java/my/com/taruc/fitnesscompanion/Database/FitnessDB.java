package my.com.taruc.fitnesscompanion.Database;

/**
 * Created by saiboon on 5/6/2015.
 */

import android.content.Context;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Util.Constant;

public class FitnessDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FitnessDb";
    private static final int DATABASE_VERSION = Constant.DB_Version;
    private static final String queryCreateUserProfile = "CREATE TABLE User(" +
            "id  VARCHAR(255) PRIMARY KEY NOT NULL," +
            "gcm_id VARCHAR(255)," +
            "email VARCHAR(255)," +
            "password VARCHAR(255)," +
            "name VARCHAR(255)," +
            "dob VARCHAR(30)," + //date format: yyyy-mm-dd
            "gender VARCHAR(10)," +
            "initial_weight Double," +
            "height  Double," +
            "reward_point INTEGER," + //defaut zero
            "created_at DateTime," +
            "updated_at DATETIME," +
            "image BLOB" +
            ");";
    private static final String queryCreateHealthProfile = "CREATE TABLE Health_Profile(" +
            "id VARCHAR(30)," +
            "user_id VARCHAR(255)," +
            "weight Double," +
            "blood_pressure INTEGER," +
            "resting_heart_rate INTEGER," +
            "arm_girth DECIMAL(6,2)," +
            "chest_girth DECIMAL(6,2)," +
            "calf_girth DECIMAL(6,2)," +
            "thigh_girth DECIMAL(6,2)," +
            "waist DECIMAL(6,2)," +
            "hip DECIMAL(6,2)," +
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)" +
            ");";

    private static final String queryCreateActivityPlans = "CREATE TABLE Activity_Plan(" +
            "id VARCHAR(30)," +
            "user_id VARCHAR(255)," +
            "type VARCHAR(255)," +
            "name VARCHAR(255)," +
            "desc VARCHAR(255)," +
            "estimate_calories DOUBLE," +
            "duration Integer," + //minutes
            "max_HR VARCHAR(255)," +
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "trainer_id INTEGER," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)" +
            ");";

    private static final String queryCreateRecord = "CREATE TABLE Fitness_Record(" +
            "id VARCHAR(30)," +
            "user_id VARCHAR(255)," +
            "activities_id VARCHAR(30)," +
            "record_duration INTEGER," +  // in sec
            "record_distance DECIMAL(6,2)," + //CHANGE AT 24/7/2015 , in meter
            "record_calories DECIMAL(6,2)," +
            "record_step INTEGER," +
            "average_heart_rate DECIMAL(6,2)," +
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)," +
            "FOREIGN KEY (activities_id) REFERENCES Activity_Plan(id)" +
            ");";

    private static final String queryCreateGoal = "CREATE TABLE Goal(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "goal_desc VARCHAR(255)," +
            "goal_target Integer," +
            "goal_duration Integer," + //day
            "goal_done BOOLEAN," +
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)" +
            ");";

    private static final String queryCreateReminder = "CREATE TABLE Reminder(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "availability BOOLEAN," +
            "activities_id VARCHAR(255)," +
            "repeat VARCHAR(255)," +
            "time VARCHAR(30)," + //change at 25/7/2015 from int to string
            "day VARCHAR(30)," +
            "date INTEGER," +
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)," +
            "FOREIGN KEY (activities_id) REFERENCES Activity_Plan(id)" +
            ");";

    private static final String queryCreateRanking = "CREATE TABLE Ranking(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "name   VARCHAR(255)," +
            "type   VARCHAR(255), " +
            "points  INTEGER," +
            "fitness_record_id VARCHAR(255)," +
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)" +
            "FOREIGN KEY (fitness_record_id) REFERENCES Fitness_Record(id)" +
            ");";

    private static final String queryCreateAchievement = "CREATE TABLE Achievement(" +
            "id VARCHAR(30)," +
            "user_id   VARCHAR(255)," +
            "milestone_name VARCHAR(255)," +
            "milestone_result BOOLEAN," +
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)" +
            ");";
    private static final String queryCreateRealTimeFitness = "CREATE TABLE RealTime_Fitness(" +
            "id VARCHAR(30)," +
            "user_id VARCHAR(255)," +
            "capture_datetime DATETIME, " +
            "step_number Integer," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)" +
            ");"; // keep track real time fitness into graph -- walking running secondary

    private static final String queryCreateEvent = "CREATE TABLE Event(" +
            "id VARCHAR(30)," +
            "banner BLOB, " +
            "url VARCHAR(555)," +
            "title VARCHAR(255), " +
            "location VARCHAR(255), " +
            "eventdate VARCHAR(255),"+
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "PRIMARY KEY (id)" +
            ");";

    private static final String queryCreateSleepData = "CREATE TABLE Sleep_Data(" +
            "id VARCHAR(30), " +
            "user_id VARCHAR(255), " +
            "movement INTEGER, " +
            "created_at DATETIME, " +
            "updated_at DATETIME, " +
            "PRIMARY KEY (id, user_id), " +
            "FOREIGN KEY (user_id) REFERENCES User(id)" +
            ");";

    private static final String queryCreateVRRecord = "CREATE TABLE VR_Record(" +
            "id VARCHAR(30)," +
            "user_id VARCHAR(255)," +
            "duration DOUBLE," +  // in sec
            "distance DOUBLE," + //CHANGE AT 24/7/2015 , in meter
            "speed INTEGER," +
            "created_at DATETIME," +
            "updated_at DATETIME," +
            "PRIMARY KEY (id, user_id)," +
            "FOREIGN KEY (user_id) REFERENCES User(id)" +
            ");";

    private static final String queryCreateFoodDetail = "CREATE TABLE Food_detail(" +
            "id INTEGER AUTO_INCREMENT," +
            "nutient VARCHAR(35)," +
            "amount DOUBLE, " +
            "unit VARCHAR(5), " +
            "FoodName VARCHAR(255), " +
            "PRIMARY KEY (id)" +
            ");";

    private static final String insert1 = "INSERT INTO Food_detail (nutient,amount,unit,FoodName) VALUES ('Energy',282.62,'k','Biscuit,chocolate chip(Biskut coklat cip)')," +
            "('Carbohydate',63.78,'g','Biscuit,chocolate chip(Biskut coklat cip)'),"+
            "('Water',3.35,'g','Biscuit,chocolate chip(Biskut coklat cip)'),"+
            "('Protein',6.13,'g','Biscuit,chocolate chip(Biskut coklat cip)'),"+
            "('Fat',21.38,'g','Biscuit,chocolate chip(Biskut coklat cip)'),"+
            "('Total Dietary Fibre,TDF',3.6,'g','Biscuit,chocolate chip(Biskut coklat cip)'),"+

            "('Energy',284.29,'k','Biscuit,corn(Biskut jagung)')," +
            "('Carbohydate',64.62,'g','Biscuit,corn(Biskut jagung)'),"+
            "('Water',2.68,'g','Biscuit,corn(Biskut jagung)'),"+
            "('Protein',7.76,'g','Biscuit,corn(Biskut jagung)'),"+
            "('Fat',18.03,'g','Biscuit,corn(Biskut jagung)'),"+
            "('Total Dietary Fibre,TDF',4.88,'g','Biscuit,corn(Biskut jagung)'),"+

            "('Energy',284.41,'k','Biscuit,cracker with sugar(Biskut kraker bergula)')," +
            "('Carbohydate',64.71,'g','Biscuit,cracker with sugar(Biskut kraker bergula)'),"+
            "('Water',3.83,'g','Biscuit,cracker with sugar(Biskut kraker bergula)'),"+
            "('Protein',8.57,'g','Biscuit,cracker with sugar(Biskut kraker bergula)'),"+
            "('Fat',17,'g','Biscuit,cracker with sugar(Biskut kraker bergula)'),"+
            "('Total Dietary Fibre,TDF',3.72,'g','Biscuit,cracker with sugar(Biskut kraker bergula)')";

    private static final String insert2 = "INSERT INTO Food_detail (nutient,amount,unit,FoodName) VALUES ('Energy',265.96,'k','Biscuit,crackers,vegetable flavor (Biskut kraker perisa sayuran)')," +
            "('Carbohydate',59.05,'g','Biscuit,crackers,vegetable flavor (Biskut kraker perisa sayuran)'),"+
            "('Water',2.93,'g','Biscuit,crackers,vegetable flavor (Biskut kraker perisa sayuran)'),"+
            "('Protein',8.27,'g','Biscuit,crackers,vegetable flavor (Biskut kraker perisa sayuran)'),"+
            "('Fat',21.48,'g','Biscuit,crackers,vegetable flavor (Biskut kraker perisa sayuran)'),"+
            "('Total Dietary Fibre,TDF',5.29,'g','Biscuit,crackers,vegetable flavor (Biskut kraker perisa sayuran)'),"+

            "('Energy',263.87,'k','Biscuit,oatmeal(Biskut oat)')," +
            "('Carbohydate',59.26,'g','Biscuit,oatmeal(Biskut oat)'),"+
            "('Water',4.35,'g','Biscuit,oatmeal(Biskut oat)'),"+
            "('Protein',8.69,'g','Biscuit,oatmeal(Biskut oat)'),"+
            "('Fat',18.14,'g','Biscuit,oatmeal(Biskut oat)'),"+
            "('Total Dietary Fibre,TDF',7.74,'g','Biscuit,oatmeal(Biskut oat)'),"+

            "('Energy',267.82,'k','Biscuit,shortbread')," +
            "('Carbohydate',59.18,'g','Biscuit,shortbread'),"+
            "('Water',4.55,'g','Biscuit,shortbread'),"+
            "('Protein',6.45,'g','Biscuit,shortbread'),"+
            "('Fat',24.67,'g','Biscuit,shortbread'),"+
            "('Total Dietary Fibre,TDF',4.02,'g','Biscuit,shortbread'),"+

            "('Energy',265.42,'k','Biscuit,wholemeal crackers(Biskut kraker mil penuh)')," +
            "('Carbohydate',59.06,'g','Biscuit,wholemeal crackers(Biskut kraker mil penuh)'),"+
            "('Water',3.06,'g','Biscuit,wholemeal crackers(Biskut kraker mil penuh)'),"+
            "('Protein',9.92,'g','Biscuit,wholemeal crackers(Biskut kraker mil penuh)'),"+
            "('Fat',19.25,'g','Biscuit,wholemeal crackers(Biskut kraker mil penuh)'),"+
            "('Total Dietary Fibre,TDF',6.5,'g','Biscuit,wholemeal crackers(Biskut kraker mil penuh)')";

    private static final String insert3 = "INSERT INTO Food_detail (nutient,amount,unit,FoodName) VALUES ('Energy',266.45,'k','Jam,apricot(Jem aprikot)')," +
            "('Carbohydate',66.45,'g','Jam,apricot(Jem aprikot)'),"+
            "('Water',32.15,'g','Jam,apricot(Jem aprikot)'),"+
            "('Protein',0.43,'g','Jam,apricot(Jem aprikot)'),"+
            "('Fat',0.2,'g','Jam,apricot(Jem aprikot)'),"+
            "('Total Dietary Fibre,TDF',0.52,'g','Jam,apricot(Jem aprikot)'),"+

            "('Energy',264.28,'k','Jam,blueberry(Jem blueberi)')," +
            "('Carbohydate',65.87,'g','Jam,blueberry(Jem blueberi)'),"+
            "('Water',33.21,'g','Jam,blueberry(Jem blueberi)'),"+
            "('Protein',0.32 ,'g','Jam,blueberry(Jem blueberi)'),"+
            "('Ash',0.12,'g','Jam,blueberry(Jem blueberi)'),"+
            "('Total Dietary Fibre,TDF',0.48,'g','Jam,blueberry(Jem blueberi)'),"+

            "('Energy',256.8,'k','Jam,grape(Jem anggur)')," +
            "('Carbohydate',64.1,'g','Jam,grape(Jem anggur)'),"+
            "('Water',35.31,'g','Jam,grape(Jem anggur)'),"+
            "('Protein',0.28 ,'g','Jam,grape(Jem anggur)'),"+
            "('Ash',0.2,'g','Jam,grape(Jem anggur)'),"+
            "('Total Dietary Fibre,TDF',0.11,'g','Jam,grape(Jem anggur)'),"+

            "('Energy',271.16,'k','Jam,strawberry(Jem,strawberi)')," +
            "('Carbohydate',67.53,'g','Jam,strawberry(Jem,strawberi)'),"+
            "('Water',31.19,'g','Jam,strawberry(Jem,strawberi)'),"+
            "('Protein',0.42 ,'g','Jam,strawberry(Jem,strawberi)'),"+
            "('Ash',0.24,'g','Jam,strawberry(Jem,strawberi)'),"+
            "('Total Dietary Fibre,TDF',0.61,'g','Jam,strawberry(Jem,strawberi)'),"+

            "('Energy',187.35,'k','Mayonnaise(Mayonis)')," +
            "('Carbohydate',39.82,'g','Mayonnaise(Mayonis)'),"+
            "('Water',30.6,'g','Mayonnaise(Mayonis)'),"+
            "('Protein',1.18 ,'g','Mayonnaise(Mayonis)'),"+
            "('Fat',26.87,'g','Mayonnaise(Mayonis)'),"+
            "('Ash',1.54,'g','Mayonnaise(Mayonis)')";

    private static final String insert4 = "INSERT INTO Food_detail (nutient,amount,unit,FoodName) VALUES ('Energy',318.79,'k','Rice,Basmati(Beras basmati)')," +
            "('Carbohydate',59.18,'g','Rice,Basmati(Beras basmati)'),"+
            "('Water',12.56,'g','Rice,Basmati(Beras basmati)'),"+
            "('Protein',8.36,'g','Rice,Basmati(Beras basmati)'),"+
            "('Fat',0.47,'g','Rice,Basmati(Beras basmati)'),"+
            "('Total Dietary Fibre,TDF',0.72,'g','Rice,Basmati(Beras basmati)'),"+

            "('Energy',313.59,'k','Rice,boil in beg rice(Ketupat)')," +
            "('Carbohydate',76.4,'g','Rice,boil in beg rice(Ketupat)'),"+
            "('Water',12.28,'g','Rice,boil in beg rice(Ketupat)'),"+
            "('Protein',7.7,'g','Rice,boil in beg rice(Ketupat)'),"+
            "('Fat',0.28,'g','Rice,boil in beg rice(Ketupat)'),"+
            "('Total Dietary Fibre,TDF',2.96,'g','Rice,boil in beg rice(Ketupat)'),"+

            "('Energy',316.88,'k','Rice,fragrant(Beras Wangi)')," +
            "('Carbohydate',77.3,'g','Rice,fragrant(Beras Wangi)'),"+
            "('Water',13.23,'g','Rice,fragrant(Beras Wangi)'),"+
            "('Protein',7.29,'g','Rice,fragrant(Beras Wangi)'),"+
            "('Fat',0.41,'g','Rice,fragrant(Beras Wangi)'),"+
            "('Total Dietary Fibre,TDF',1.48,'g','Rice,fragrant(Beras Wangi)'),"+

            "('Energy',323.85,'k','Rice,siam(Beras siam)')," +
            "('Carbohydate',78.96,'g','Rice,siam(Beras siam)'),"+
            "('Water',12.16,'g','Rice,siam(Beras siam)'),"+
            "('Protein',7.57,'g','Rice,siam(Beras siam)'),"+
            "('Fat',0.43,'g','Rice,siam(Beras siam)'),"+
            "('Total Dietary Fibre,TDF',0.58,'g','Rice,siam(Beras siam)'),"+

            "('Energy',121.76,'k','Sauce,thousand island')," +
            "('Carbohydate',23.9,'g','Sauce,thousand island'),"+
            "('Water',46.67,'g','Sauce,thousand island'),"+
            "('Protein',1.05,'g','Sauce,thousand island'),"+
            "('Fat',25.12,'g','Sauce,thousand island'),"+
            "('Total Dietary Fibre,TDF',0.88,'g','Sauce,thousand island'),"+

            "('Energy',260.92,'k','Wafer,chocolate,full coated(Wafer salut coklat)')," +
            "('Carbohydate',57.06,'g','Wafer,chocolate,full coated(Wafer salut coklat)'),"+
            "('Water',1.95,'g','Wafer,chocolate,full coated(Wafer salut coklat)'),"+
            "('Protein',6.61,'g','Wafer,chocolate,full coated(Wafer salut coklat)'),"+
            "('Fat',26.05,'g','Wafer,chocolate,full coated(Wafer salut coklat)'),"+
            "('Total Dietary Fibre,TDF',6.54,'g','Wafer,chocolate,full coated(Wafer salut coklat)')";


    private static final String dropTableUserProfile = "DROP TABLE IF EXISTS User ";
    private static final String dropTableHealthProfile = "DROP TABLE IF EXISTS Health_Profile ";
    private static final String dropTableGoal = "DROP TABLE IF EXISTS Goal ";
    private static final String dropTableRecord = "DROP TABLE IF EXISTS Fitness_Record ";
    private static final String dropTableReminder = "DROP TABLE IF EXISTS Reminder ";
    private static final String dropTableAchievement = "DROP TABLE IF EXISTS Achievement ";
    private static final String dropTableRealTimeFitness = "DROP TABLE IF EXISTS RealTime_Fitness ";
    private static final String dropTableActivityPlans = "DROP TABLE IF EXISTS Activity_Plan ";
    private static final String dropTableRanking = "DROP TABLE IF EXISTS Ranking ";
    private static final String dropTableEvent = "DROP TABLE IF EXISTS Event ";
    private static final String dropTableSleepData = "DROP TABLE IF EXISTS Sleep_Data ";
    private static final String dropTableVRRecord = "DROP TABLE IF EXISTS VR_Record ";
    private static final String dropTableFDetail = "DROP TABLE IF EXISTS Food_detail ";

    private Context context;
    private Boolean result;
    ArrayList<String> mylist = new ArrayList<String>();


    public FitnessDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(queryCreateFoodDetail);
            db.execSQL(queryCreateVRRecord);
            db.execSQL(queryCreateUserProfile);
            db.execSQL(queryCreateHealthProfile);
            db.execSQL(queryCreateActivityPlans);
            db.execSQL(queryCreateRecord);
            db.execSQL(queryCreateGoal);
            db.execSQL(queryCreateReminder);
            db.execSQL(queryCreateRanking);
            db.execSQL(queryCreateAchievement);
            db.execSQL(queryCreateRealTimeFitness);
            db.execSQL(queryCreateEvent);
            db.execSQL(queryCreateSleepData);
        db.execSQL(insert1);
        db.execSQL(insert2);
        db.execSQL(insert3);
        db.execSQL(insert4);
            result = doesDatabaseExist(context, DATABASE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int upgradeTo = oldVersion + 1;
        try {
            while (upgradeTo <= newVersion) {
                switch (upgradeTo) {
                    case 2:
//                        db.execSQL(Constant.alter_table_activityplan);
                        break;
                    case 3:
//                        db.execSQL(Constant.alter_table_event);
                        break;
                }
                upgradeTo++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        Log.d("Database:", "Database Updated");
        Toast.makeText(context, "Database Updated", Toast.LENGTH_LONG).show();
    }

    public void InitialData() {
        InputStream in;
        BufferedReader reader;
        String line;
        Integer count = 0;
        try {
            AssetManager am = context.getAssets();
            InputStream inputStream = am.open("test.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            line = reader.readLine();
            while (line != null) {
                mylist.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}





