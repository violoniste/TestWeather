package irongate.testweather.model.openweatherAPI;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Iron on 28.01.2018.
 */

public class APIForecast {
    final public String time;
    final public Integer temp;
    final public JSONObject jsonObject;

    private APIForecast(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        String time = null;
        Integer temp = null;
        try {
            JSONObject main = jsonObject.getJSONObject("main");
            temp = main.getInt("temp");
            time = jsonObject.getString("dt_txt");
        } catch (JSONException e) {
            Log.d("IRON", "APIForecast.APIForecast() " + e);
        }
        this.time = time;
        this.temp = temp;
    }

    public static ArrayList<APIForecast> parseList(String json) {
        ArrayList<APIForecast> newList = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray list = jsonObject.getJSONArray("list");
            newList = new ArrayList<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject o = list.getJSONObject(i);
                newList.add(new APIForecast(o));
            }
        } catch (JSONException e) {
            Log.d("IRON", "APICity.parseList() " + e);
        }
        return newList;
    }
}
