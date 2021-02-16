package ir.sourcearena.boursezone.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.StrictMode;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import ir.sourcearena.boursezone.Settings;

public class NetworkChecker {

    Context ctx;
    public NetworkChecker(Context ctx){
        this.ctx = ctx;
    }
    public boolean isServerAvailable() {
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
        try {
            URL url = new URL(Settings.SERVER);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            if (httpURLConnection.getResponseCode() == 200) {
                return true;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
        public boolean isNetworkAvailable() throws NullPointerException {
            boolean res = true;
        try {
            ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

             res = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }catch (NullPointerException | SecurityException t){

        }
        return res;
    }
}
