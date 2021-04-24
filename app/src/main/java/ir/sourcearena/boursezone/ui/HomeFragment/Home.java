package ir.sourcearena.boursezone.ui.HomeFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;

import com.baoyz.widget.PullRefreshLayout;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.voela.actrans.AcTrans;
import ir.sourcearena.boursezone.FieldsUtil;
import ir.sourcearena.boursezone.ui.firstfilter.FilterUtils;
import ir.sourcearena.boursezone.NamadRouter;
import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.ui.firstfilter.RecyclerAdapter;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.ui.LoadingView;
import androidx.appcompat.app.AppCompatDelegate;
public class Home extends Fragment {
    CarouselView customCarouselView, customCarouselView2;
    View root;
    PullRefreshLayout ref;
    SharedPreferences preferences;
    LoadingView loading;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fu = new FieldsUtil(getContext());

        root = inflater.inflate(R.layout.homestub, container, false);
        ViewStub stub = (ViewStub) root.findViewById(R.id.stub);
        View inflated = stub.inflate();
        loading = new LoadingView(inflater, root,getActivity());
        root = inflated;


        preferences = (Home.this).getActivity().getSharedPreferences("Splash", Context.MODE_PRIVATE);

        customCarouselView = (CarouselView) root.findViewById(R.id.carouselView);
        customCarouselView2 = (CarouselView) root.findViewById(R.id.carouselView2);
        customCarouselView.setSlideInterval(15000);
        customCarouselView.setIndicatorVisibility(View.GONE);
        //customCarouselView.setIndicatorGravity(Gravity.TOP);
        refresh();
        ref = (PullRefreshLayout) root.findViewById(R.id.refresher);
        ref.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        ref.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });


        return loading.addLoadingBar(false);
    }

    List<DataEntry> b = new ArrayList<>();

    public void refresh() {
        b1 = new ArrayList<>();
        ;
        b2 = new ArrayList<>();
        ;
        b3 = new ArrayList<>();
        ;
        prepareAll();


    }

    private void alllArrayParse(String responseString) throws JSONException {


        final JSONArray ja = new JSONArray(responseString);


        for (int i = 0; i < ja.length(); i++) {

            final int finalI = i;

            if (finalI == 9) {
                loading.cancel();
            }
            switch (finalI) {
                case 0:

                    try {
                        ParseJSon2(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;

                case 1:
                    try {
                        ParseJSon2(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 2:

                    try {
                        ParseJSon2(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
                case 3:
                    try {
                        qnumber(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        ParseJSon(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case 5:
                    try {
                        ParseJSon(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case 6:
                    try {
                        ParseJSon(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case 7:
                    try {
                        ParseJSon(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case 8:
                    try {
                        ParseJSon(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
                case 9:

                    try {
                        ParseJSon(ja.get(finalI).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    break;
            }


        }


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
                    alllArrayParse(result);
               } catch (JSONException | NullPointerException d) {
                        d.printStackTrace();

                }


            }
        }
    }


    void prepareAll() {

        new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"https://sourcearena.ir/androidFilterApi/first_page_app/app.php");


    }

    FieldsUtil fu;


    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<FilterUtils> utils;
    String cf1, cf2, cf3;


    public void ParseJSon(String data) throws JSONException {


        //loading.cancel();
        JSONArray js = new JSONArray(data);
        utils = new ArrayList<>();


        int id = 0;
        for (int i = 0; i < 5; i++) {

            JSONObject obj = js.getJSONObject(i);

            id = obj.getInt("id");
        }

        String field = "";

        switch (id) {
            case 0:
            case 1:
                field = "co_change";
                break;
            case 2:
                field = "trade_volume";
                break;
            case 3:
                field = "trade_value";
                break;
            case 4:
                field = "sell_q_vol";
                break;
            case 5:
                field = "buy_q_vol";
                break;

        }

        for (int i = 0; i < 5; i++) {
            int n = 0;
            JSONObject obj = js.getJSONObject(i);

            cf1 = obj.getString(field);


            cf3 = obj.getString("close_price_change");

            cf2 = obj.getString("closeprice");
            id = obj.getInt("id");


            utils.add(new FilterUtils((i + 1) + ". " + obj.getString("name").split(" \\(")[0],
                    cf1, cf2, cf3));


        }
        int cardId = 0;

        switch (id) {
            case 0:
                id = R.id.first_filter_home;
                break;
            case 1:
                id = R.id.second_filter_home;
                break;
            case 2:
                id = R.id.third_filter_home;
                break;
            case 3:
                id = R.id.forth_filter_home;
                break;
            case 4:
                id = R.id.fifth_filter_home;
                break;
            case 5:

                id = R.id.sixth_filter_home;
                break;

        }

        recyclerView = root.findViewById(id);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new
                RecyclerAdapter(getContext(), utils, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(FilterUtils item) {

                String name = item.getF1();

                name = name.replace(".", " ");
                name = name.split("  ")[1];
                Intent in = new Intent(getContext(), NamadRouter.class);
                in.putExtra(Settings.TITLE_EXTRA, name);
                in.putExtra(Settings.APPEND_URL, "&name=" + name);
                startActivity(in);
                new AcTrans.Builder(getContext()).performFade();

            }
        });
        recyclerView.setAdapter(mAdapter);
        recyclerView.setFocusable(false);


    }

    ViewListener viewListener2 = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {

            View customView = getLayoutInflater().inflate(R.layout.carousel_view2, null);

            TextView tv1 = customView.findViewById(R.id.market_name);
            TextView tv2 = customView.findViewById(R.id.index_caro);
            TextView tv3 = customView.findViewById(R.id.index_change_caro);
            TextView tv4 = customView.findViewById(R.id.change_price_caro);
            tv1.setText("تغییرات صف خرید و فروش");
            tv4.setText("خرید: " + c4[0]);
            tv3.setText("فروش: " + c4[1]);
            tv2.setText("کل: " + c4[2]);
            root.findViewById(R.id.cd2).setVisibility(View.VISIBLE);




            ref.setRefreshing(false);

            preferences.edit().putInt("chart", preferences.getInt("chart", 0) + 1).apply();

            WebView wv = customView.findViewById(R.id.webchart);
            wv.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.lightcard,null));
            WebSettings settings = wv.getSettings();
            settings.setDomStorageEnabled(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            settings.setJavaScriptEnabled(true);
            wv.loadUrl("http://sourcearena.ir/charts/index.php?bg="+getResources().getString(R.color.lightcard));
            return customView;
        }
    };

    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.carousel_view2, null);

            root.findViewById(R.id.cd1).setVisibility(View.VISIBLE);

            TextView tv1 = customView.findViewById(R.id.market_name);
            TextView tv2 = customView.findViewById(R.id.index_caro);
            TextView tv3 = customView.findViewById(R.id.index_change_caro);
            TextView tv4 = customView.findViewById(R.id.change_price_caro);




            if (position == 0) {
                tv1.setText("شاخص کل بورس");
                tv2.setText(c1[0]);
                if (c1[1].contains("-")) {
                    tv3.setTextColor(getResources().getColor(R.color.red));
                    tv4.setTextColor(getResources().getColor(R.color.red));
                } else {
                    tv3.setTextColor(getResources().getColor(R.color.green));
                    tv4.setTextColor(getResources().getColor(R.color.green));
                }
                tv3.setText(c1[1]);
                tv4.setText(c1[2] + "%");

            }

            if (position == 2) {
                tv1.setText("شاخص کل فرابورس");
                tv2.setText(c3[0]);
                if (c3[1].contains("-")) {

                    tv3.setTextColor(getResources().getColor(R.color.red));
                    tv4.setTextColor(getResources().getColor(R.color.red));
                } else {

                    tv3.setTextColor(getResources().getColor(R.color.green));
                    tv4.setTextColor(getResources().getColor(R.color.green));
                }
                tv3.setText(c3[1]);
                tv4.setText(c3[2] + "%");


            }
            if (position == 1) {
                tv1.setText("شاخص هم وزن");
                tv2.setText(c2[0]);
                tv3.setText(c2[1]);
                tv4.setText(c2[2] + "%");


                if (c2[1].contains("-")) {

                    tv3.setTextColor(getResources().getColor(R.color.red));
                    tv4.setTextColor(getResources().getColor(R.color.red));
                } else {

                    tv3.setTextColor(getResources().getColor(R.color.green));
                    tv4.setTextColor(getResources().getColor(R.color.green));
                }
            }





            preferences.edit().putInt("chart", preferences.getInt("chart", 0) + 1).commit();

            int color = ResourcesCompat.getColor(getResources(),R.color.lightcard,null);
            WebView wv = customView.findViewById(R.id.webchart);
            wv.setBackgroundColor(color);
            WebSettings settings = wv.getSettings();
            settings.setDomStorageEnabled(true);
            settings.setAllowFileAccess(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
            wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            settings.setJavaScriptEnabled(true);
            wv.loadUrl("http://sourcearena.ir/charts/sh.php?mode="+position+"&bg="+getResources().getString(R.color.lightcard).replace("#",""));
            return customView;
        }
    };

    public static Home newInstance(String text) {

        Home f = new Home();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);

        }

    }

    private class QnData extends ValueDataEntry {

        QnData(String x, Number value, Number value2) {
            super(x, value);
            setValue("value2", value2);

        }

    }

    List<DataEntry> b1 = new ArrayList<>();
    List<DataEntry> b2 = new ArrayList<>();
    List<DataEntry> b3 = new ArrayList<>();
    List<DataEntry> b4 = new ArrayList<>();
    String[] c1, c2, c3, c4;

    private void qnumber(String data) throws JSONException {

        JSONObject obj = new JSONObject(data);
        String index = obj.getString("buy");
        String index_change = obj.getString("sell");
        String percent = obj.getString("all");

        c4 = new String[]{index, index_change, percent};

        JSONArray ja = obj.getJSONArray("data");
        for (int i = 0; i < ja.length(); i++) {
            b4.add(new QnData(ja.getJSONObject(i).getString("t"), ja.getJSONObject(i).getInt("b"), ja.getJSONObject(i).getInt("s")));

        }



        customCarouselView2.setViewListener(viewListener2);
        customCarouselView2.setPageCount(1);
    }


    public void ParseJSon2(String data) throws JSONException {


        JSONObject obj = new JSONObject(data);
        String index = obj.getString("index");
        String index_change = obj.getString("change");
        String percent = obj.getString("percent");
        if (obj.getInt("type") == 0) {
            b = b1;
            c1 = new String[]{index, index_change, percent};
        } else if (obj.getInt("type") == 1) {
            b = b2;
            c2 = new String[]{index, index_change, percent};
        } else if (obj.getInt("type") == 2) {
            b = b3;
            c3 = new String[]{index, index_change, percent};
        }
        JSONArray ja = obj.getJSONArray("data");
        for (int i = 0; i < ja.length(); i++) {
            b.add(new CustomDataEntry(ja.getJSONObject(i).getString("t"), ja.getJSONObject(i).getInt("i")));

        }
        if (b2.size() > 0) {
            customCarouselView.setViewListener(viewListener);
            customCarouselView.setPageCount(3);



        }
    }


    int po = 0;


}