package irongate.testweather.view.details;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import irongate.testweather.R;
import irongate.testweather.model.openweatherAPI.APICity;
import irongate.testweather.view.forecast.ForecastView;

/**
 * Created by Iron on 05.02.2018.
 */

public class DetailsView implements IDetailsView, View.OnClickListener {

    private final DetailsPresenter presenter;
    private final ForecastView forecastView;
    private Activity activity;
    private int id;

    public DetailsView(Activity activity) {
        this.activity = activity;
        presenter = new DetailsPresenter(this);

        forecastView = new ForecastView(activity);

        activity.findViewById(R.id.btnFore3).setOnClickListener(this);
        activity.findViewById(R.id.btnFore5).setOnClickListener(this);
        activity.findViewById(R.id.btnRemove).setOnClickListener(this);
        activity.findViewById(R.id.btnDetailsBack).setOnClickListener(this);
    }

    public void showDetails(int id) {
        this.id = id;
        activity.findViewById(R.id.contDetails).setVisibility(View.VISIBLE);
        String name = presenter.getNameById(id);
        TextView textName = activity.findViewById(R.id.textDetailsName);
        textName.setText(name);
        ((TextView)activity.findViewById(R.id.textDetailsTemp)).setText("-");
        ((TextView)activity.findViewById(R.id.textPressure)).setText("-");
        ((TextView)activity.findViewById(R.id.textHumidity)).setText("-");
        ((TextView)activity.findViewById(R.id.textSpeed)).setText("-");
        presenter.getCurrent(id);
    }

    @Override
    public void onCurrent(String current) {
        if (current == null) {
            return;
        }
        APICity city = APICity.parse(current);
        populateFields(city);
    }

    @SuppressLint("SetTextI18n")
    private void populateFields(APICity city) {
        String tempStr = city.temp + " " + activity.getResources().getString(R.string.degrees_celsius);
        if (city.temp > 0)
            tempStr = "+" + tempStr;

        ((TextView)activity.findViewById(R.id.textDetailsTemp)).setText(tempStr);
        ((TextView)activity.findViewById(R.id.textPressure)).setText(city.pressure + " hPa");
        ((TextView)activity.findViewById(R.id.textHumidity)).setText(city.humidity + "%");
        ((TextView)activity.findViewById(R.id.textSpeed)).setText(city.speed + " m/s");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFore3:
                forecastView.showForecast(id, 3);
                break;

            case R.id.btnFore5:
                forecastView.showForecast(id, 5);
                break;

            case R.id.btnRemove:
                activity.findViewById(R.id.contDetails).setVisibility(View.GONE);
                presenter.removeCity(id);
                break;

            case R.id.btnDetailsBack:
                activity.findViewById(R.id.contDetails).setVisibility(View.GONE);
                break;
        }
    }
}
