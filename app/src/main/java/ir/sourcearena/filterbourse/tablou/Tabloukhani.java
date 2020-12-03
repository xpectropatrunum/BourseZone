package ir.sourcearena.filterbourse.tablou;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;

public class Tabloukhani extends Fragment {
    View root;

    Settings setting;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.tabloukhani, container, false);
        String append = getActivity().getIntent().getExtras().getString("title", "");
        task = new Tabloukhani.Request().execute(setting.JSON_TABLOU_KAHNI + append);

        return root;
    }

    public void ParseJSon(String data) throws JSONException {
        ViewHelper vh = new ViewHelper();
        int[] vs = vh.views;
        String[] key = vh.keys;
        Log.e("e", data);
        JSONObject jo = new JSONObject(data);

        for (int i = 0; i < key.length; i++) {


            TextView tc = (root.findViewById(vs[i]));
            tc.setText(jo.getString(key[i]));
        }


    }

    private class Request extends AsyncTask<String, Void, String> {

        StringBuffer stringBuffer;

        @Override
        protected String doInBackground(String... params) {

            try {


                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                DataOutputStream outputStream = new DataOutputStream(httpURLConnection.getOutputStream());


                int code = httpURLConnection.getResponseCode();
                if (code == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuffer = new StringBuffer();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }


                }

            } catch (Exception e) {

                e.printStackTrace();
            }
            return stringBuffer.toString();


        }

        @Override
        protected void onPostExecute(String result) {
            if (getActivity().isChangingConfigurations()) {
                onDestroy();
            } else {
                try {
                    ParseJSon(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getFragmentManager().beginTransaction().remove((Fragment) this).commitAllowingStateLoss();

    }

    AsyncTask task;

    @Override
    public void onPause() {
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
            task = null;
        }

        super.onPause();
    }
}
