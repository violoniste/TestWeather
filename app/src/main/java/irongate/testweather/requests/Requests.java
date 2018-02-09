package irongate.testweather.requests;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Iron on 02.02.2018.
 */

public class Requests {
    private static final int READ_TIMEOUT = 5000;
    private static final int CONNECTION_TIMEOUT = 5000;

    static public void getRequest(String url, GetRequestListener listener) {
        new Thread(new GetRequestRun(url, listener)).start();
    }

    static private class GetRequestRun implements Runnable {
        URL url;
        final GetRequestListener listener;

        GetRequestRun(String urlString, GetRequestListener listener) {
            super();
            this.listener = listener;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                Log.d("IRON", "GetRequestTask: invalid URL:" + urlString + e);
            }
        }

        @Override
        public void run() {
            if (url == null)
                return;

            HttpURLConnection connection;
            InputStream inputStream;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                try {
                    connection.connect();
                }
                catch (SecurityException se) {
                    Log.d("IRON", "Requests: SecurityException: " + se);
                    if (listener != null)
                        listener.onException(se);
                    return;
                }

                // Если ответ не нужен
                if (listener == null)
                    return;

                inputStream = connection.getInputStream();
            }
            catch (IOException ex) {
                Log.d("IRON", "Requests: connection fail: " + ex);
                if (listener != null)
                    listener.onException(ex);
                return;
            }

            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String inputLine;
            try {
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
            } catch (IOException e) {
                Log.d("IRON", "Requests: Reading fail: " + e);
            }
            try {
                streamReader.close();
            } catch (IOException e) {
                Log.d("IRON", "Requests: " + e);
            }

            String response = stringBuilder.toString();
            listener.onResponse(response);
        }
    }
}
