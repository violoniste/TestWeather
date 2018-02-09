package irongate.testweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Iron on 01.02.2018.
 */

public class DBWeather extends SQLiteOpenHelper {
    static private DBWeather inst;
    private static DBListener listener;

    static private final int DB_VERSION = 1;
    static private final String DB_NAME = "WeatherDB";

    static private final String TABLE_NAME = "wtable";
    private boolean created = false;

    public DBWeather(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        inst = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (id int primary key, name text, current text, forecast text);");
        created = true;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public static void setListener(DBListener listener) {
        DBWeather.listener = listener;
    }

    public static void addCityData(int id, String name, String current, String forecast) {
        SQLiteDatabase db = inst.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("name", name);
        cv.put("current", current);
        cv.put("forecast", forecast);
        try {
            db.replaceOrThrow(TABLE_NAME, null, cv);
        }
        catch (Exception ex) {
            Log.d("IRON", "addCityData: " + ex.getMessage());
        }
        inst.close();
    }

    public static void getCurrent(int id) {
        new Thread(new GetCurrentRun(id)).start();
    }

    static class GetCurrentRun implements Runnable {
        private int id;

        private GetCurrentRun(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            SQLiteDatabase db = inst.getReadableDatabase();
            String[] columns = { "current" };
            String selection = "id = " + id;
            Cursor c = db.query(TABLE_NAME, columns, selection, null, null, null, null);
            if (!c.moveToFirst()) {
                c.close();
                listener.onDBCurrent(id, null);
                return;
            }
            int currentIndex = c.getColumnIndex("current");
            String current = c.getString(currentIndex);
            c.close();
            listener.onDBCurrent(id, current);
        }
    }

    static public void updateCurrent(int id, String current) {
        new Thread(new UpdateCurrentRun(id, current)).start();
    }

    static class UpdateCurrentRun implements Runnable {
        private int id;
        private String current;

        private UpdateCurrentRun(int id, String current) {
            this.id = id;
            this.current = current;
        }

        @Override
        public void run() {
            SQLiteDatabase db = inst.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("current", current);
            db.update(TABLE_NAME, cv, "id = " + id, null);
            inst.close();
        }
    }

    public static void getForecast(int id) {
        new Thread(new GetForecastRun(id)).start();
    }

    static class GetForecastRun implements Runnable {
        private int id;

        private GetForecastRun(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            SQLiteDatabase db = inst.getReadableDatabase();
            String[] columns = { "forecast" };
            String selection = "id = " + id;
            Cursor c = db.query(TABLE_NAME, columns, selection, null, null, null, null);
            if (!c.moveToFirst()) {
                c.close();
                listener.onDBForecast(id, null);
                return;
            }
            String forecast = c.getString(c.getColumnIndex("forecast"));
            c.close();
            listener.onDBForecast(id, forecast);
        }
    }

    static public void updateForecast(int id, String forecast) {
        new Thread(new UpdateForecastRun(id, forecast)).start();
    }

    static class UpdateForecastRun implements Runnable {
        private int id;
        private String forecast;

        private UpdateForecastRun(int id, String forecast) {
            this.id = id;
            this.forecast = forecast;
        }

        @Override
        public void run() {
            SQLiteDatabase db = inst.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("forecast", forecast);
            db.update(TABLE_NAME, cv, "id = " + id, null);
            inst.close();
        }
    }

    static  public void getList() {
        new Thread(new GetListRun()).start();
    }

    static class GetListRun implements Runnable {
        GetListRun() {

        }

        @Override
        public void run() {
            SQLiteDatabase db = inst.getReadableDatabase();
            String[] columns = { "id", "name", "current" };
            Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);
            if (!c.moveToFirst()) {
                c.close();
                listener.onDBList(null);
                return;
            }
            int idIndex = c.getColumnIndex("id");
            int nameIndex = c.getColumnIndex("name");
            int currentIndex = c.getColumnIndex("current");
            int forecastIndex = c.getColumnIndex("forecast");
            ArrayList<DBCity> dbCities = new ArrayList<>();
            do {
                int id = c.getInt(idIndex);
                String name = c.getString(nameIndex);
                String currentJSON = c.getString(currentIndex);
                DBCity dbCity = new DBCity(id, name, currentJSON);
                dbCities.add(dbCity);
            } while (c.moveToNext());
            c.close();
            listener.onDBList(dbCities);
        }
    }

    public static void removeCity(int id) {
        SQLiteDatabase db = inst.getWritableDatabase();
        db.delete(TABLE_NAME, "id = " + id, null);
    }

    public static boolean isCreated() {
        return inst.created;
    }
}
