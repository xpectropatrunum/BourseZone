package ir.sourcearena.filterbourse.fundamental.java;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import ir.sourcearena.filterbourse.NamadActivity;
import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;
import ir.sourcearena.filterbourse.ui.LoadingView;

public class Fundamental extends Fragment {
    View root;

    Settings setting;
    LoadingView loading;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        root = inflater.inflate(R.layout.fundamental, container, false);
        loading = new LoadingView(inflater,root,getActivity());
        String append = getActivity().getIntent().getExtras().getString("title", "");
        task = new Fundamental.Request().execute(setting.JSON_FUNDAMENTAL + append);
        cd1 = root.findViewById(R.id.cardView2);
        cd2 = root.findViewById(R.id.pro_card);

        return  loading.addLoadingBar(false);
    }
    CardView cd1,cd2;
    @Override
    public void onPause() {
        super.onPause();

        if(task != null)task.cancel(true);
    }
    public static Fundamental newInstance() {

        Fundamental f = new Fundamental();


        return f;
    }
    public void ParseJSon(String data) throws JSONException {
        ViewHelper vh = new ViewHelper();
        int[] vs = vh.views;
        String[] key = vh.keys;
        Log.e("e", data);
        JSONObject jo = new JSONObject(data);

        for (int i = 0; i < key.length; i++) {
            String text = jo.getString(key[i]);
            TextView tc = (root.findViewById(vs[i]));
            tc.setText(text);

        }
        if(!jo.getString("rate").equals("")){

            cd2.setVisibility(View.VISIBLE);
        }
        cd1.setVisibility(View.VISIBLE);
        loading.cancel();

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



    AsyncTask task;


}
