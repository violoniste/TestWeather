package irongate.testweather.db;

import java.util.List;

/**
 * Created by Iron on 01.02.2018.
 */

public interface DBListener {
    void onDBList(List<DBCity> list);
    void onDBCurrent(int id, String current);
    void onDBForecast(int id, String forecast);
}
