package irongate.testweather.view.forecast;

/**
 * Created by Iron on 06.02.2018.
 */

public interface IForecastView {
    void onForecast(String forecast);
    void onForecastError();
}
