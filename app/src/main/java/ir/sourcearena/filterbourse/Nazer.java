package ir.sourcearena.filterbourse;

import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ir.sourcearena.filterbourse.ui.LoadingView;

public class Nazer extends Fragment {
    View root;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    Settings setting;
    List<FilterUtils> utils;
    LoadingView loading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.frag_nazer, container, false);
        loading = new LoadingView(inflater, root, getActivity());
        recyclerView = (RecyclerView) root.findViewById(R.id.nazer_cycle);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        String append = getActivity().getIntent().getExtras().getString(Settings.TITLE_EXTRA, "");

        task = new Nazer.Request().execute(setting.JSON_INSPECT_TITLES + append);

        return loading.addLoadingBar(false);
    }

    public void ParseJSon(String data) throws JSONException {
        utils = new ArrayList<>();
        JSONArray jA = new JSONArray(data);
        if (jA.length() == 0) {
            TextView tc = (root.findViewById(R.id.tnazer));
            tc.setText("پیامی وجود ندارد");
        }
        for (int i = 0; i < jA.length(); i++) {
            JSONObject obj = jA.getJSONObject(i);
            Log.e("d", obj.getString("time"));
            utils.add(new FilterUtils("", "",
                    obj.getString("head"), obj.getString("time"), obj.getString("text"), "", "", "", "cfn3"));
        }
        mAdapter = new NazerAdapter(getContext(), utils);

        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        loading.cancel();


    }

    @Override
    public void onPause() {
        super.onPause();

        if (task != null) task.cancel(true);
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

                newThread(result);

            }
        }
    }

    void newThread(final String result) {
        new Thread(new Runnable() {


            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            ParseJSon(result);
                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }
                    }

        });
    }

    }).start();

}

    AsyncTask task;

    public static Nazer newInstance() {

        Nazer f = new Nazer();

        return f;
    }
}
