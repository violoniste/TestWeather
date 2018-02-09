package irongate.testweather.view.forecast;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import irongate.testweather.R;

/**
 * Created by Iron on 28.01.2018.
 */

public class ForecastFragment extends Fragment {
    private String time;
    private Integer temp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forecast, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (time != null)
            update();
    }

    public void setFields(String time, Integer temp) {
        this.time = time;
        this.temp = temp;
        update();
    }

    @SuppressLint("SetTextI18n")
    private void update() {
        View view = getView();
        if (view == null)
            return;

        String[] split = time.split(" ");
        String timeStr = split[1].substring(0, 5);
        String dateStr = split[0].substring(5);

        TextView textTime = view.findViewById(R.id.textForeTime);
        textTime.setText(dateStr + " " + timeStr);

        String tempStr = "...";
        if (temp != null) {
            tempStr = temp + " " + getResources().getString(R.string.degrees_celsius);

            if (temp > 0) {
                tempStr = "+" + tempStr;
            }
        }
        TextView textTemp = view.findViewById(R.id.textForeTemp);
        textTemp.setText(tempStr);
    }
}
