package irongate.testweather.db;

/**
 * Created by Iron on 01.02.2018.
 */

public class DBCity {
    final public int id;
    final public String name;
    final public String currentJSON;

    DBCity(int id, String name, String currentJSON) {
        this.id = id;
        this.name = name;
        this.currentJSON = currentJSON;
    }
}
