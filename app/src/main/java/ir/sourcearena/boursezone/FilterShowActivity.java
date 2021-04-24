package ir.sourcearena.boursezone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import id.voela.actrans.AcTrans;
import ir.sourcearena.boursezone.ui.LoadingView;

public class FilterShowActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    FieldsUtil fu;
    List<FilterUtils> utils;
    LoadingView loading;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View root = getLayoutInflater().inflate(R.layout.filter_lists, null);
        SharedPreferences s = getSharedPreferences("per", Context.MODE_PRIVATE);
        s.edit().putBoolean("permitted",false).apply();
        loading = new LoadingView(getLayoutInflater(),root,this);
        setContentView(loading.addLoadingBar(false));


        setTileActionbar();
        shareBtn();

        loadHelpers();
        accessNet();
        defRecycler();
        getData();


    }

    private void shareBtn() {
        ImageView im = findViewById(R.id.share_icon);
        ImageView back = findViewById(R.id.imageView3);
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation(view, 10000, Techniques.Pulse,false);
                share_filter();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation(view, 10000, Techniques.ZoomIn,false);
                finish();
            }
        });
    }
    int p = 0 ;
    private void setTileActionbar() {
        String title = getIntent().getExtras().getString("title", "g");

        final TextView tit = findViewById(R.id.app_name);

        tit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p++%3 == 1){
                    animation(tit, 10000, Techniques.Tada,false);
                }

            }
        });
        share += title + "\n";
        tit.setText(title);
    }
    public void animation(final View view, final int time, final Techniques t, final boolean repeat) {
        final Handler handler = new Handler();
        if(repeat) {
            final Runnable r = new Runnable() {
                public void run() {
                    YoYo.with(t)
                            .duration(700)
                            .playOn(view);

                    handler.postDelayed(this, time);


                }
            };


            handler.postDelayed(r, time);
        }else{
            YoYo.with(t)
                    .duration(500)
                    .playOn(view);
        }



    }

    private void loadHelpers() {
        fu = new FieldsUtil(getBaseContext());

    }

    private void accessNet() {
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
    }

    AsyncTask task = null;
    private void getData() {
        String append = getIntent().getExtras().getString("appendix", "");
        task = new Request().execute(Settings.JSON_API_URL + append);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(task != null){
            task.cancel(true);
        };
    }

    String share = "";
    String cf1, cf2, cf3;
    String cfn1, cfn2, cfn3;

    public void ParseJSon(String data) throws JSONException {
        loading.cancel();

        JSONArray js = new JSONArray(data);
        utils = new ArrayList<>();


        for (int i = 0; i < js.length(); i++) {
            int n = 0;
            JSONObject obj = js.getJSONObject(i);
            Iterator<String> iter = obj.keys();
            while (iter.hasNext()) {
                String key = iter.next();
                if (key.contains("_")) {
                    try {
                        n++;
                        Object value = obj.get(key);
                        if (n == 1) {
                            cf1 = value.toString();
                            cfn1 = fu.getLocale(key);
                        }
                        if (n == 2) {
                            cf2 = value.toString();
                            cfn2 = fu.getLocale(key);
                        }
                        if (n == 3) {
                            cf3 = value.toString();
                            cfn3 = fu.getLocale(key);
                            n = 0;
                        }


                    } catch (JSONException e) {
                        // Something went wrong!
                    }
                }

            }
            utils.add(new FilterUtils((i + 1) + ". " + obj.getString("name"), "",
                    "", cf1, cf2, cf3, cfn1, cfn2, cfn3));
            share += (i + 1) + ". " + obj.getString("name") + "\n";


        }
        TextView tv = findViewById(R.id.no_user_filter_text2);
        if(utils.size() == 0){
            tv.setVisibility(View.VISIBLE);

        }
        mAdapter = new RecyclerAdapter(this, utils, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilterUtils item) {
                String dl = " \\(";
                String name = item.getName().split(dl)[0];
                name = name.replace(".", " ");
                name = name.split("  ")[1];

                Intent in = new Intent(FilterShowActivity.this, NamadRouter.class);
                in.putExtra(Settings.TITLE_EXTRA, name);
                in.putExtra(Settings.APPEND_URL, "&name=" + name);
                startActivity(in);
                new AcTrans.Builder(FilterShowActivity.this).performFade();

            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setFocusable(true);
        recyclerView.requestFocus();


    }

    public void share_filter() {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        String shareBody = share + "اپلیکیشن " + getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.bazaar_link);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "فیتر");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "اشتراک گذاری فیلتر"));
    }


    private void defRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewContainer);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }

    private class Request extends AsyncTask<String, Void, String> {

        StringBuffer stringBuffer;

        @Override
        protected String doInBackground(String... params) {
            stringBuffer = null;
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
            if (stringBuffer == null) {
                return "";
            }
            return stringBuffer.toString();


        }

        int p = 0;

        @Override
        protected void onPostExecute(final String result) {


            if (!result.equals("")) {


                try {
                    ParseJSon(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
    }


}