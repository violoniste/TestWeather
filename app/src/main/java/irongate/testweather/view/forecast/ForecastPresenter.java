package irongate.testweather.view.forecast;

import irongate.testweather.model.Main;

/**
 * Created by Iron on 06.02.2018.
 */

 class ForecastPresenter implements Main.ForecastListener{
    private IForecastView view;

    ForecastPresenter(IForecastView view) {
        this.view = view;
        Main.getInstance().setForecastListener(this);
    }

    String getNameById(int id) {
        return Main.getInstance().getNameById(id);
    }

    void getForecast(int id) {
        Main.getInstance().getForecast(id);
    }

    @Override
    public void onForecast(String forecast) {
        view.onForecast(forecast);
    }

    @Override
    public void onForecastError() {
        view.onForecastError();
    }
}
