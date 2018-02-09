package irongate.testweather.model.openweatherAPI;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Iron on 28.01.2018.
 */

public class APICity {
    final public Integer id;
    final public String name;
    final public Integer temp;
    final public Integer pressure;
    final public Integer humidity;
    final public Double speed;
    final public JSONObject jsonObject;

    private APICity(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        Integer id = null;
        String name = null;
        Integer temp = null;
        Integer pressure = null;
        Integer humidity = null;
        Double speed = null;
        try {
            id = jsonObject.getInt("id");
            name = jsonObject.getString("name");
            JSONObject main = jsonObject.getJSONObject("main");
            temp = main.getInt("temp");
            pressure = main.getInt("pressure");
            humidity = main.getInt("humidity");
            JSONObject wind = jsonObject.getJSONObject("wind");
            speed = wind.getDouble("speed");
        } catch (JSONException e) {
            Log.d("IRON", "APICity.APICity() " + e);
        }
        this.id = id;
        this.name = name;
        this.temp = temp;
        this.pressure = pressure;
        this.humidity = humidity;
        this.speed = speed;
    }

    public static APICity parse(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            Log.d("IRON", "APICity.APICity() " + e);
        }
        return new APICity(jsonObject);
    }

    public static ArrayList<APICity> parseList(String json) {
        ArrayList<APICity> apiList = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray list = jsonObject.getJSONArray("list");
            apiList = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject o = list.getJSONObject(i);
                apiList.add(new APICity(o));
            }
        } catch (JSONException e) {
            Log.d("IRON", "APICity.parseList() " + e);
        }
        return apiList;
    }

    public String toString() {
        return id + " " + name;
    }
}
