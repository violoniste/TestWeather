package irongate.testweather.model;

/**
 * Created by Iron on 28.01.2018.
 */

public class ListCity {
    final public int id;
    final public String name;
    final public Integer temp;

    ListCity(int id, String name, Integer temp) {
        this.id = id;
        this.name = name;
        this.temp = temp;
    }

    public String toString() {
        return id + " " + name;
    }
}
