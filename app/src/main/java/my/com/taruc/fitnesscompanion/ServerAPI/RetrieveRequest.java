package my.com.taruc.fitnesscompanion.ServerAPI;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import my.com.taruc.fitnesscompanion.Classes.Achievement;
import my.com.taruc.fitnesscompanion.Classes.ActivityPlan;
import my.com.taruc.fitnesscompanion.Classes.DateTime;
import my.com.taruc.fitnesscompanion.Classes.Event;
import my.com.taruc.fitnesscompanion.Classes.FitnessRecord;
import my.com.taruc.fitnesscompanion.Classes.Goal;
import my.com.taruc.fitnesscompanion.Classes.RealTimeFitness;
import my.com.taruc.fitnesscompanion.Classes.Reminder;
import my.com.taruc.fitnesscompanion.Classes.SleepData;
import my.com.taruc.fitnesscompanion.Util.DbBitmapUtility;

/**
 * Created by Hexa-Jackson on 12/27/2015.
 */
public class RetrieveRequest  {
    public static final int CONNECTION_TIMEOUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://www.tarucfit.pe.hu/ServerRequest/";
    private static final String TAG_RESULTS = "result";

    public RetrieveRequest(Context context) {

    }

    public ArrayList<Goal> fetchAllGoalInBackground(String userID){
        ArrayList<Goal> mGoalRecordArrayList = new ArrayList<Goal>();
        try {
            FetchAllGoalRecordAsyncTask fetch = new FetchAllGoalRecordAsyncTask(userID);
            fetch.execute();
            mGoalRecordArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return mGoalRecordArrayList;
    }

    public ArrayList<FitnessRecord> fetchAllFitnessRecordInBackground(String userID){
        ArrayList<FitnessRecord> mFitnessRecordArrayList = new ArrayList<FitnessRecord>();
        try {
            FetchAllFitnessRecordAsyncTask fetch = new FetchAllFitnessRecordAsyncTask(userID);
            fetch.execute();
            mFitnessRecordArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return mFitnessRecordArrayList;
    }

    public ArrayList<Reminder> fetchAllReminderInBackground(String userID){
        ArrayList<Reminder> mReminderArrayList = new ArrayList<Reminder>();
        try {
            FetchAllReminderAsyncTask fetch = new FetchAllReminderAsyncTask(userID);
            fetch.execute();
            mReminderArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return mReminderArrayList;
    }

    public ArrayList<RealTimeFitness> fetchAllRealTimeFitnessInBackground(String userID){
        ArrayList<RealTimeFitness>  mRealTimeFitnessArrayList = new ArrayList<RealTimeFitness>();
        try {
            FetchAllRealTimeFitnessAsyncTask fetch = new FetchAllRealTimeFitnessAsyncTask(userID);
            fetch.execute();
            mRealTimeFitnessArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return mRealTimeFitnessArrayList;
    }

    public ArrayList<SleepData> fetchAllSleepDataInBackground(String userID){
        ArrayList<SleepData>  mSleepDataArrayList = new ArrayList<SleepData>();
        try {
            FetchAllSleepDataAsyncTask fetch = new FetchAllSleepDataAsyncTask(userID);
            fetch.execute();
            mSleepDataArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return mSleepDataArrayList;
    }

    public ArrayList<Event> fetchAllEventInBackground(){
        ArrayList<Event> mEventArrayList = new ArrayList<Event>();
        try {
            FetchAllEventAsyncTask fetch = new FetchAllEventAsyncTask();
            fetch.execute();
            mEventArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return mEventArrayList;
    }

    public FitnessRecord fetchFitnessRecord(String id, String userId){
        FitnessRecord fitnessRecord = null;
        try {
            FetchFitnessRecordAsyncTask fetch = new FetchFitnessRecordAsyncTask(id,userId);
            fetch.execute();
            fitnessRecord = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return fitnessRecord;
    }

    public ArrayList<String> fetchAllEmailInBackground(){
        ArrayList<String> mEmailArrayList = new ArrayList<String>();
        try {
            FetchEmailAsyncTask fetch = new FetchEmailAsyncTask();
            fetch.execute();
            mEmailArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return mEmailArrayList;
    }

    public ArrayList<ActivityPlan> fetchActivityPlanDataInBackground(String userID){
        ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<ActivityPlan>();
        try {
            FetchActivityPlanAsyncTask fetch = new FetchActivityPlanAsyncTask(userID);
            fetch.execute();
            activityPlanArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return activityPlanArrayList;
    }

    public ArrayList<Achievement> fetchAchievementInBackground(String userID){
        ArrayList<Achievement> achievementArrayList = new ArrayList<Achievement>();
        try {
            FetchAchievementAsyncTask fetch = new FetchAchievementAsyncTask(userID);
            fetch.execute();
            achievementArrayList = fetch.get();
        }  catch(Exception ex) {
            ex.printStackTrace();
        }
        return achievementArrayList;
    }

    public class FetchAllGoalRecordAsyncTask extends AsyncTask<Void, Void, ArrayList<Goal>> {

        String userId;

        public FetchAllGoalRecordAsyncTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected ArrayList<Goal> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<Goal> goalArrayList = new ArrayList<Goal>();
            Goal goal = null;
            boolean goal_done;
            JSONArray jsonArray = null;

            try {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("user_id", userId));
                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchAllGoalRecord.php");
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();

                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String goalID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        String goal_desc = jObject.getString("goal_desc");
                        Integer goal_duration = jObject.getInt("goal_duration");
                        Integer goal_target = jObject.getInt("goal_target");
                        Integer goal_done_int = jObject.getInt("goal_done");
                        if (goal_done_int == 0) {
                            goal_done = false;
                        } else {
                            goal_done = true;
                        }
                        String created_at =  jObject.getString("created_at");
                        String updated_at =  jObject.getString("updated_at");
                        goal = new Goal(goalID, userID, goal_desc, goal_target, goal_duration, goal_done, new DateTime(created_at), new DateTime(updated_at));
                        goalArrayList.add(goal);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return goalArrayList;
        }
    }

    public class FetchAllFitnessRecordAsyncTask extends AsyncTask<Void, Void, ArrayList<FitnessRecord>> {

        String userId;

        public FetchAllFitnessRecordAsyncTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected ArrayList<FitnessRecord> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<FitnessRecord> sleepDataArrayList = new ArrayList<FitnessRecord>();
            FitnessRecord fitnessRecord = null;
            JSONArray jsonArray = null;

            try {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("user_id", userId));
                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchAllFitnessRecord.php");
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();

                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Fitness Record", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String fitnessID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        Integer record_distance = jObject.getInt("record_distance");
                        Integer record_duration = jObject.getInt("record_duration");
                        Double record_calories = jObject.getDouble("record_calories");
                        Integer record_step = jObject.getInt("record_step");
                        Double average_heart_rate = jObject.getDouble("average_heart_rate");
                        String created_at =  jObject.getString("created_at");
                        String updated_at =  jObject.getString("updated_at");
                        String activity_id =  jObject.getString("activity_id");
                        fitnessRecord = new FitnessRecord(fitnessID, userID, activity_id, record_duration, record_distance, record_calories
                                , record_step, average_heart_rate, new DateTime(created_at), new DateTime(updated_at));
                        sleepDataArrayList.add(fitnessRecord);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sleepDataArrayList;
        }
    }

    public class FetchAllReminderAsyncTask extends AsyncTask<Void, Void, ArrayList<Reminder>> {

        String userId;

        public FetchAllReminderAsyncTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected ArrayList<Reminder> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<Reminder> reminderArrayList = new ArrayList<Reminder>();
            Reminder reminder = null;
            JSONArray jsonArray = null;

            try {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("user_id", userId));
                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchAllReminder.php");
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();

                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Reminder", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String reminderID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        boolean availability = true;
                        if(jObject.getInt("availability")==0){
                            availability = false;
                        }
                        String activity_id =  jObject.getString("activity_id");
                        String RemindRepeat = jObject.getString("repeats");
                        String RemindTime = jObject.getString("time");
                        String[] RemindTimeSplit = RemindTime.split(":");
                        String RemindTimeFormatted = RemindTimeSplit[0] + RemindTimeSplit[1];

                        String RemindDay = jObject.getString("day");
                        Integer RemindDate = jObject.getInt("date");
                        String created_at =  jObject.getString("created_at");
                        String updated_at =  jObject.getString("updated_at");
                        reminder = new Reminder(reminderID, userID, availability, activity_id, RemindRepeat, RemindTimeFormatted
                                , RemindDay, RemindDate, new DateTime(created_at), new DateTime(updated_at));
                        reminderArrayList.add(reminder);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return reminderArrayList;
        }
    }

    public class FetchAllRealTimeFitnessAsyncTask extends AsyncTask<Void, Void, ArrayList<RealTimeFitness>> {

        String userId;
        public FetchAllRealTimeFitnessAsyncTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected ArrayList<RealTimeFitness> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<RealTimeFitness> sleepDataArrayList = new ArrayList<RealTimeFitness>();
            RealTimeFitness realTimeFitness = null;
            JSONArray jsonArray = null;

            try {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("user_id", userId));
                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchRealTimeFitnessRecord.php");
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();

                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Event", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String stepID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        Integer step_number = jObject.getInt("step_number");
                        String createdAt =  jObject.getString("capture_datetime");
                        realTimeFitness = new RealTimeFitness(stepID, userID, new DateTime(createdAt), step_number);
                        sleepDataArrayList.add(realTimeFitness);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sleepDataArrayList;
        }
    }

    public class FetchAllSleepDataAsyncTask extends AsyncTask<Void, Void, ArrayList<SleepData>> {
        String userId;
        public FetchAllSleepDataAsyncTask(String userId) {
            this.userId = userId;
        }

        @Override
        protected ArrayList<SleepData> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<SleepData> sleepDataArrayList = new ArrayList<SleepData>();
            SleepData sleepData = null;
            JSONArray jsonArray = null;

            try {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("user_id", userId));
                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchSleepDataRecord.php");
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();

                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Sleep", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String sleepDataID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        Integer movement = jObject.getInt("movement");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        sleepData = new SleepData(sleepDataID, userID, movement, new DateTime(createdAt), new DateTime(updatedAt));
                        sleepDataArrayList.add(sleepData);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return sleepDataArrayList;
        }
    }


    public class FetchAllEventAsyncTask extends AsyncTask<Void, Void, ArrayList<Event>> {

        public FetchAllEventAsyncTask() {

        }

        @Override
        protected ArrayList<Event> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<Event> eventArrayList = new ArrayList<Event>();
            Event event = null;
            JSONArray jsonArray = null;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchEventRecord.php");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Event", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String eventID = jObject.getString("id");
                        String banner = jObject.getString("banner");
                        String url = jObject.getString("url");
                        String title = jObject.getString("title");
                        String location = jObject.getString("location");
                        String eventDate = jObject.getString("eventdate");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        Bitmap returnBitmap = DbBitmapUtility.getImageFromJSon(banner);
                        event = new Event(eventID, returnBitmap, url, title, location, eventDate, new DateTime(createdAt), new DateTime(updatedAt));
                        eventArrayList.add(event);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return eventArrayList;
        }
    }


    public class FetchFitnessRecordAsyncTask extends AsyncTask<Void, Void, FitnessRecord> {

        String id, userId;

        public FetchFitnessRecordAsyncTask(String id, String userId) {
            this.id = id;
            this.userId = userId;
        }

        @Override
        protected FitnessRecord doInBackground(Void... params) {
            String result = null;
            FitnessRecord fitnessRecord = null;

            try {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("id", id));
                dataToSend.add(new BasicNameValuePair("userID", userId));
                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchFitnessRecord.php");

                try {
                    post.setEntity(new UrlEncodedFormEntity(dataToSend));
                    HttpResponse httpRespond = client.execute(post);

                    HttpEntity entity = httpRespond.getEntity();
                    result = EntityUtils.toString(entity);

                    JSONObject jObject = new JSONObject(result);

                    if (jObject.length() != 0) {
                        String fitnessRecordID = jObject.getString("id");
                        String userID = jObject.getString("userID");
                        int record_duration = jObject.getInt("record_duration");
                        double record_distance = jObject.getDouble("record_distance");
                        double record_calories = jObject.getDouble("record_calories");
                        int record_step =  jObject.getInt("record_step");
                        double average_heart_rate = jObject.getInt("average_heart_rate");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        String activity_id = jObject.getString("activity_id");
                        fitnessRecord= new FitnessRecord(fitnessRecordID, userID, activity_id, record_duration, record_distance
                                , record_calories, record_step, average_heart_rate, new DateTime(createdAt), new DateTime(updatedAt));
                        return fitnessRecord;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return fitnessRecord;
        }
    }




    public class FetchActivityPlanAsyncTask extends AsyncTask<Void, Void, ArrayList<ActivityPlan>> {

        String userID;

        public FetchActivityPlanAsyncTask(String userID) {
            this.userID = userID;
        }

        @Override
        protected ArrayList<ActivityPlan> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<ActivityPlan> activityPlanArrayList = new ArrayList<ActivityPlan>();
            ActivityPlan activityPlan = null;
            JSONArray jsonArray = null;

            try {
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("user_id", userID));
                HttpParams httpRequestParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
                HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);

                HttpClient client = new DefaultHttpClient(httpRequestParams);
                HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchActivityPlanRecord.php");

                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                HttpResponse httpRespond = client.execute(post);

                HttpEntity entity = httpRespond.getEntity();



//                DefaultHttpClient httpClient = new DefaultHttpClient();
//                HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchActivityPlanRecord.php");
//                HttpResponse httpResponse = httpClient.execute(httpGet);
//                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Activity Plan", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String activityPlanID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        String type = jObject.getString("type");
                        String activityName = jObject.getString("name");
                        String description = jObject.getString("description");
                        double estimateCalories =  jObject.getDouble("estimate_calories");
                        int duration = jObject.getInt("duration");
                        double maxHR =  jObject.getDouble("max_HR");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        int trainer_id = jObject.getInt("trainer_id");
                        activityPlan = new ActivityPlan(activityPlanID, userID, type, activityName, description, estimateCalories, duration, maxHR, new DateTime(createdAt), new DateTime(updatedAt), trainer_id);
                        activityPlanArrayList.add(activityPlan);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return activityPlanArrayList;
        }
    }



    public class FetchAchievementAsyncTask extends AsyncTask<Void, Void, ArrayList<Achievement>> {
        String userID;

        public FetchAchievementAsyncTask(String userID) {
            this.userID = userID;
        }

        @Override
        protected ArrayList<Achievement> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            ArrayList<Achievement> achievementArrayList = new ArrayList<Achievement>();
            Achievement achievement = null;
            JSONArray jsonArray = null;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                ArrayList<NameValuePair> dataToSend = new ArrayList<>();
                dataToSend.add(new BasicNameValuePair("user_id", userID));
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchAchievementRecord.php"+"?"+ URLEncodedUtils.format(dataToSend, "utf-8"));
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Activity Plan", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String achievementID = jObject.getString("id");
                        String userID = jObject.getString("user_id");
                        String milestoneName = jObject.getString("milestones_name");
                        boolean milestoneResult = jObject.getBoolean("milestones_result");
                        String createdAt =  jObject.getString("created_at");
                        String updatedAt = jObject.getString("updated_at");
                        achievement = new Achievement(achievementID, userID, milestoneName, milestoneResult, new DateTime(createdAt), new DateTime(updatedAt));
                        achievementArrayList.add(achievement);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return achievementArrayList;
        }
    }


    public class FetchEmailAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        public FetchEmailAsyncTask() {
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = null;
            String myJSON;
            JSONArray jsonArray = null;
            ArrayList<String> emailArrayList = new ArrayList<>();

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(SERVER_ADDRESS + "FetchAllEmail.php");
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();

                inputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                result = sb.toString();
                Log.d("Email", result);
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    jsonArray = jsonObj.getJSONArray(TAG_RESULTS);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jObject = jsonArray.getJSONObject(i);
                        String email = jObject.getString("email");
                        emailArrayList.add(email);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return emailArrayList;
        }
    }


}
