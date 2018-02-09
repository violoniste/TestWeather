package irongate.testweather.model.openweatherAPI;

/**
 * Created by Iron on 02.02.2018.
 */

public interface OpenWeatherAPIListener {
    void onCurrentList(String json);
    void onCurrentByName(String name, String json);
    void onForecastById(int id, String json);
    void errorCurrentList();
    void errorForecast();
    void errorCityNotFound(String name);
    void errorCityCurrent();
}
