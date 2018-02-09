package irongate.testweather.model.openweatherAPI;

import android.text.TextUtils;
import android.util.Log;

import java.io.FileNotFoundException;
import java.util.List;

import irongate.testweather.requests.GetRequestListener;
import irongate.testweather.requests.Requests;

/**
 * Created by Iron on 28.01.2018.
 */

public class OpenweatherAPI {
    static final private String API_URL = "http://api.openweathermap.org/data/2.5/";
    static final private String APP_ID = "5643eb59b147af23df5e932c6350f530";

    private OpenWeatherAPIListener listener;

    public OpenweatherAPI(OpenWeatherAPIListener listener) {
        this.listener = listener;
    }

    public void getCurrentByIds(List<Integer> ids) {
        String idsAsStr = TextUtils.join(",", ids);
        String url = API_URL + "group?id=" + idsAsStr + "&units=metric&appid=" + APP_ID;
        Requests.getRequest(url, new GetRequestListener() {
            @Override
            public void onResponse(String response) {
                listener.onCurrentList(response);
            }

            @Override
            public void onException(Exception ex) {
                listener.errorCurrentList();
            }
        });
    }

    public void getCurrentByName(final String name) {
        String url = API_URL + "weather?q=" + name + "&units=metric&appid=" + APP_ID;
        Requests.getRequest(url, new GetRequestListener() {
            @Override
            public void onResponse(String response) {
                listener.onCurrentByName(name, response);
            }
            @Override
            public void onException(Exception ex) {
                if (ex instanceof FileNotFoundException)
                    listener.errorCityNotFound(name);
                else
                    listener.errorCityCurrent();
            }
        });
    }

    public void getForecastById(final int id) {
        String url = API_URL + "forecast?id=" + id + "&units=metric&appid=" + APP_ID;
        Requests.getRequest(url, new GetRequestListener() {
            @Override
            public void onResponse(String response) {
                listener.onForecastById(id, response);
            }
            @Override
            public void onException(Exception ex) {
                listener.errorForecast();
            }
        });
    }
}
