package irongate.testweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import irongate.testweather.model.Main;
import irongate.testweather.db.DBWeather;
import irongate.testweather.view.MainView;

public class MainActivity extends AppCompatActivity {
    private MainView mainView;
    static public DBWeather db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBWeather(this);
        mainView = new MainView(this);
        Main.getInstance().init();
    }
}
