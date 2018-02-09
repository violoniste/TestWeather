package irongate.testweather.view;

import android.app.Activity;
import android.widget.Toast;

import irongate.testweather.view.mainList.MainListView;

/**
 * Created by Iron on 04.02.2018.
 */

public class MainView implements IMainView {
    private final Presenter presenter;
    private MainListView mainListView;
    private Activity activity;

    public MainView(Activity activity) {
        this.activity = activity;
        presenter = new Presenter(this);

        mainListView = new MainListView(activity);
    }

    @Override
    public void showErrorCityAlreadyAdded() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "City already added!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showErrorCityNotFound() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "City not found!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void showErrorCityData() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "City data error!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
