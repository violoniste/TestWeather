package irongate.testweather.view.forecast;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import irongate.testweather.R;
import irongate.testweather.model.openweatherAPI.APIForecast;

/**
 * Created by Iron on 06.02.2018.
 */

public class ForecastView implements IForecastView, View.OnClickListener {
    private final ForecastPresenter presenter;
    private Activity activity;
    private ArrayList<ForecastFragment> foreFragments = new ArrayList<>();
    private int days;

    public ForecastView(Activity activity) {
        this.activity = activity;

        presenter = new ForecastPresenter(this);
    }

    public void showForecast(int id, int days) {
        this.days = days;
        activity.findViewById(R.id.contForecast).setVisibility(View.VISIBLE);
        String name = presenter.getNameById(id);
        TextView textName = activity.findViewById(R.id.textForecastName);
        textName.setText(name);

        activity.findViewById(R.id.btnForecastBack).setOnClickListener(this);

        clearList();
        presenter.getForecast(id);
    }

    @Override
    public void onForecast(String forecast) {
        clearList();
        foreFragments = new ArrayList<>();

        if (forecast == null)
            return;

        ArrayList<APIForecast> list = APIForecast.parseList(forecast);
        int nFores = days * 8;  // Количество прогнозов, исходя из того, что их 8 в день
        List<APIForecast> crop = list.subList(0, Math.min(nFores, list.size()));
        for (APIForecast fore:crop) {
            ForecastFragment fragment = new ForecastFragment();
            fragment.setFields(fore.time, fore.temp);
            activity.getFragmentManager().beginTransaction().add(R.id.forecastLinearLayout, fragment).commit();
            foreFragments.add(fragment);
        }
    }

    @Override
    public void onForecastError() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Forecast data error!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearList() {
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        for (ForecastFragment fragment:foreFragments) {
            ft.remove(fragment);
        }
        ft.commit();
    }

    @Override
    public void onClick(View v) {
        activity.findViewById(R.id.contForecast).setVisibility(View.GONE);
    }
}
