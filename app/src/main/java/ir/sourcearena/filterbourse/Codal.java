package ir.sourcearena.filterbourse;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ir.sourcearena.filterbourse.ui.LoadingView;

public class Codal extends Fragment {
    View root;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    Settings setting;
    List<FilterUtils> utils;
    LoadingView loading;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setRetainInstance(true);
        root = inflater.inflate(R.layout.frag_codal, container, false);
        loading = new LoadingView(inflater,root,getActivity());
        recyclerView = (RecyclerView) root.findViewById(R.id.codal_cycle);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        String append = getActivity().getIntent().getExtras().getString("title", "");

        task = new Codal.Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,setting.JSON_CODAL_TITLES + append);

        return loading.addLoadingBar(false);
    }
    public static Codal newInstance() {

        Codal f = new Codal();

        return f;
    }
    @Override
    public void onPause() {
        super.onPause();

        if(task != null)task.cancel(true);
    }
    public void ParseJSon(String data) throws JSONException {
        utils = new ArrayList<>();
        JSONArray jA = new JSONArray(data);
        for(int i =0; i<jA.length(); i++){
            JSONObject obj = jA.getJSONObject(i);
            utils.add(new FilterUtils(obj.getString("symbol"),obj.getString("name"),
                    obj.getString("letter_number"),obj.getString("date"),obj.getString("title"),obj.getString("link"),obj.getString("pdf"),obj.getString("excel"),"cfn3"));
        }
        mAdapter = new CodalAdapter(getContext(),utils, new CodalAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(url));
                startActivity(browserIntent);

            }
        });

        recyclerView.setAdapter(mAdapter);
        recyclerView.setVisibility(View.VISIBLE);
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
            if(getActivity().isChangingConfigurations()){
               onDestroy();
            }else{


                newThread(result);
        }}
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

}
