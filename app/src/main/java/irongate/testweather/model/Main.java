package irongate.testweather.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import irongate.testweather.db.DBCity;
import irongate.testweather.db.DBWeather;
import irongate.testweather.db.DBListener;
import irongate.testweather.model.openweatherAPI.APICity;
import irongate.testweather.model.openweatherAPI.OpenWeatherAPIListener;
import irongate.testweather.model.openweatherAPI.OpenweatherAPI;

/**
 * Created by Iron on 28.01.2018.
 */

public class Main implements DBListener, OpenWeatherAPIListener {
    static private Main instance;
    private OpenweatherAPI api;
    private Listener listener;
    private ListListener listListener;
    private DetailsListener detailsListener;
    private ForecastListener forecastListener;

    private ArrayList<ListCity> citiesList = new ArrayList<>();

    private boolean alreadyUpdated = false;

    private Main() {
        api = new OpenweatherAPI(this);
    }

    public static Main getInstance() {
        if (instance == null)
            instance = new Main();

        return instance;
    }

    public void init() {
        DBWeather.setListener(this);
        DBWeather.getList();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setListListener(ListListener listListener) {
        this.listListener = listListener;
    }

    public void setDetailListener(DetailsListener detailsListener) {
        this.detailsListener = detailsListener;
    }

    public void setForecastListener(ForecastListener forecastListener) {
        this.forecastListener = forecastListener;
    }

    @Override
    synchronized public void onDBList(List<DBCity> list) {
        if (list == null) {
            listListener.onListChanged();

            if (DBWeather.isCreated()) {   // Пустой потому, что база только создалась
                DBWeather.addCityData(524901, "Москва", null, null);
                DBWeather.addCityData(498817, "Санкт-Петербург", null, null);
                DBWeather.getList();
            }
            return;
        }

        citiesList = new ArrayList<>();
        for (DBCity dbCity:list) {
            Integer temp = null;
            if (dbCity.currentJSON != null) {
                APICity apiCity = APICity.parse(dbCity.currentJSON);
                temp = apiCity.temp;
            }
            ListCity listCity = new ListCity(dbCity.id, dbCity.name, temp);
            citiesList.add(listCity);
        }
        listListener.onListChanged();

        if (!alreadyUpdated)    // Список получили, но еще не обновляли инфу с сайта
            updateWeather();
    }

    private void updateWeather() {
        ArrayList<Integer> ids = new ArrayList<>();
        for (ListCity city:citiesList) {
            ids.add(city.id);
        }
        api.getCurrentByIds(ids);
    }

    @Override
    public void onCurrentList(String json) {
        ArrayList<APICity> apiCities = APICity.parseList(json);
        ArrayList<ListCity> newList = new ArrayList<>();
        for (APICity apiCity:apiCities) {
            String name = getNameById(apiCity.id);
            newList.add(new ListCity(apiCity.id, name, apiCity.temp));
            DBWeather.updateCurrent(apiCity.id, apiCity.jsonObject.toString());
        }
        citiesList = newList;
        alreadyUpdated = true;
        listListener.onListChanged();
    }

    @Override
    public void errorCurrentList() {
        listListener.onUpdateError();
    }

    public void addNewCity(String name) {
        for (ListCity city:citiesList) {
            if (Objects.equals(city.name, name)) {
                listener.onErrorCityAlreadyAdded();
                return;
            }
        }
        api.getCurrentByName(name);
    }

    @Override
    public void onCurrentByName(String name, String json) {
        int id;
        try {
            JSONObject jsonObject = new JSONObject(json);
            id = jsonObject.getInt("id");
        } catch (JSONException e) {
            Log.d("IRON", "Main.onCurrentByName() " + e);
            return;
        }
        DBWeather.addCityData(id, name, json, null);
        DBWeather.getList();
    }

    @Override
    public void errorCityNotFound(final String name) {
        listener.onErrorCityNotFound();
    }

    @Override
    public void errorCityCurrent() {
        listener.onErrorCityData();
    }

    public String getNameById(int id) {
        for (ListCity city:citiesList) {
            if (city.id == id)
                return city.name;
        }
        return null;
    }

    public List<ListCity> getCitiesList() {
        return Collections.unmodifiableList(citiesList);
    }

    public void getCurrent(int id) {
        DBWeather.getCurrent(id);
    }

    @Override
    public void onDBCurrent(int id, String current) {
        detailsListener.onCurrent(current);
    }

    public void getForecast(int id) {
        DBWeather.getForecast(id);
    }

    @Override
    public void onDBForecast(int id, String forecast) {
        forecastListener.onForecast(forecast);
        api.getForecastById(id);
    }

    @Override
    public void onForecastById(int id, String forecast) {
        DBWeather.updateForecast(id, forecast);
        forecastListener.onForecast(forecast);
    }

    @Override
    public void errorForecast() {
        forecastListener.onForecastError();
    }

    public void removeCity(int id) {
        DBWeather.removeCity(id);
        DBWeather.getList();
    }

    public interface Listener {
        void onErrorCityAlreadyAdded();
        void onErrorCityNotFound();
        void onErrorCityData();
    }

    public interface ListListener {
        void onListChanged();
        void onUpdateError();
    }

    public interface DetailsListener {
        void onCurrent(String current);
    }

    public interface ForecastListener {
        void onForecast(String forecast);
        void onForecastError();
    }
}
