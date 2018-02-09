package irongate.testweather.requests;

/**
 * Created by Iron on 02.02.2018.
 */

public interface GetRequestListener {
    void onResponse(String response);
    void onException(Exception ex);
}
