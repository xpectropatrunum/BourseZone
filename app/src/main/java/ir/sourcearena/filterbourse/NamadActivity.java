package ir.sourcearena.filterbourse;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ActionMenuView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Stock;
import com.anychart.core.stock.Plot;
import com.anychart.data.Table;
import com.anychart.data.TableMapping;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import ir.sourcearena.filterbourse.tools.NetworkChecker;
import ir.sourcearena.filterbourse.ui.HomeFragment.Home;
import ir.sourcearena.filterbourse.ui.LoadingView;
import ir.sourcearena.filterbourse.ui.TableHelper;
import ir.sourcearena.filterbourse.ui.watcher.Watchlist;

public class NamadActivity extends Fragment {


    FieldsUtil fu;
    Settings setting;
    ProgressBar pb;
    TextView co_buy, co_sell, real_buy, real_sell;
    TextView tv12, tv13, tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tvt4, tvt5, tvt6, tvt7, tvt8, tvt9, tvt10, tvt11, pb_high, pb_low;
    ImageView loc,loc2;
    View v1, v2, v3, v4;
    LinearLayout pb_con;
    View root;
    AsyncTask task;

    public static NamadActivity newInstance() {

        NamadActivity f = new NamadActivity();
        return f;
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value, Number value1, Number value2, Number value3) {
            super(x, value);
            setValue("x", x);
            setValue("high", value);
            setValue("low", value1);
            setValue("open", value2);
            setValue("close", value3);


        }

    }

    LoadingView loading;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.stock_page_activity, container, false);
        loading = new LoadingView(inflater, root, getActivity());


        defViews();
        loadHelpers();
        accessNet();
        getData();

        return loading.addLoadingBar(false);

    }


    View[] views;
    CardView dc, uc, center;

    private void defViews() {
        dc = root.findViewById(R.id.down_stock_card);
        uc = root.findViewById(R.id.top_stock_card);
        center = root.findViewById(R.id.info_card);
        tv1 = root.findViewById(R.id.ins_name);
        tv2 = root.findViewById(R.id.ins_full_name);
        tv3 = root.findViewById(R.id.ins_industry);
        tv4 = root.findViewById(R.id.ins_f_1_a);
        tv5 = root.findViewById(R.id.ins_f_2_a);
        tv6 = root.findViewById(R.id.ins_f_3_a);
        tv7 = root.findViewById(R.id.ins_f_4_a);
        tv8 = root.findViewById(R.id.ins_f_5_a);
        tv9 = root.findViewById(R.id.ins_f_6_a);
        tv10 = root.findViewById(R.id.ins_f_7_a);
        tv11 = root.findViewById(R.id.ins_f_8_a);
        tv12 = root.findViewById(R.id.ins_close_price);
        tv13 = root.findViewById(R.id.ins_final_price);
        tvt4 = root.findViewById(R.id.ins_f_1);
        tvt5 = root.findViewById(R.id.ins_f_2);
        tvt6 = root.findViewById(R.id.ins_f_3);
        tvt7 = root.findViewById(R.id.ins_f_4);
        tvt8 = root.findViewById(R.id.ins_f_5);
        tvt9 = root.findViewById(R.id.ins_f_6);
        tvt10 = root.findViewById(R.id.ins_f_7);
        tvt11 = root.findViewById(R.id.ins_f_8);

        views = new View[]{tv1, tv2, tv3, tv4, tv5, tv6, tv7, tv8, tv9, tv10, tv11, tv12, tv13, tvt4, tvt5, tvt6, tvt7, tvt8, tvt9, tvt10, tvt11};
        pb_high = root.findViewById(R.id.ins_high);
        pb_low = root.findViewById(R.id.ins_low);
        pb_con = root.findViewById(R.id.ins_pb_con);
        co_buy = root.findViewById(R.id.ins_co_buy);
        co_sell = root.findViewById(R.id.ins_co_sell);
        real_buy = root.findViewById(R.id.ins_real_buy);
        real_sell = root.findViewById(R.id.ins_real_sell);
        pb = root.findViewById(R.id.ins_pg_over);
        loc = root.findViewById(R.id.ins_close_loc);
        loc2 = root.findViewById(R.id.ins_finl_loc);
    }

    int w, w2 = 0;

    private void progressConf() {
        v1 = root.findViewById(R.id.view);
        v2 = root.findViewById(R.id.view2);
        v3 = root.findViewById(R.id.view3);
        v4 = root.findViewById(R.id.view4);

        final ProgressBar pc = (ProgressBar) root.findViewById(R.id.ins_progress);
        pc.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if (pc.getViewTreeObserver().isAlive())
                    pc.getViewTreeObserver().removeOnPreDrawListener(this);
                if(w == 0) {
                    w = pc.getWidth();
                }
                setPadding();
                return true;
            }
        });
        v1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                if (v1.getViewTreeObserver().isAlive()) {
                    v1.getViewTreeObserver().removeOnPreDrawListener(this);

                    if(w2 == 0){
                        w2 = v1.getWidth();
                    }


                    viewSize();
                }
                return true;
            }
        });


    }

    public String format(int number) {
        double amount = Double.parseDouble(number + "");
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount);
    }

    public void setPadding() {
        double left_p = (Float.parseFloat(String.valueOf((min - low) /
                Float.parseFloat(String.valueOf((high - low))))));
        double left_c = (Float.parseFloat(String.valueOf((close - low) /
                Float.parseFloat(String.valueOf((high - low))))));
        float right_p = (Float.parseFloat(String.valueOf((high - max) /
                Float.parseFloat(String.valueOf((high - low))))));
        double left_c_final = (Float.parseFloat(String.valueOf((final_p - low) /
                Float.parseFloat(String.valueOf((high - low))))));
        pb_high.setText(format(high));
        pb_low.setText(format(low));
        pb_con.setPadding(
                (int) (w * left_p) + 10,
                0,
                (int) (w * right_p) + 10,
                0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins((int) (w * left_c_final), 0, 0, 0);
       loc2.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params2.setMargins((int) (w * left_c), 0, 0, 0);
        loc.setLayoutParams(params2);





    }


    private void loadHelpers() {
        fu = new FieldsUtil();
        setting = new Settings();
    }

    private void accessNet() {
        StrictMode.ThreadPolicy tp = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(tp);
    }

    String title;


    AsyncTask task2;
    private void getData() {

        title = getActivity().getIntent().getExtras().getString(Settings.TITLE_EXTRA, "");
        if (task == null) {
           task = new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Settings.JSON_CANDLE + title);
            task2 =new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Settings.JSON_INS_URL + title);


            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {

            } else {
                final Handler handler = new Handler();

                final Runnable r = new Runnable() {
                    public void run() {
                        if(permiteed) {
                            Log.e("per2", permiteed + "");
                            if (new NetworkChecker(getContext()).isNetworkAvailable()) {
                              task =   new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Settings.JSON_INS_URL + title);
                            }
                            handler.postDelayed(this, 5000);

                        }
                    }
                };

                handler.postDelayed(r, 15000);
            }
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        permiteed = false;
        if(task != null)
        {
            task.cancel(true);
        }
         if(task2 != null)
        {
            task2.cancel(true);
        }

    }

    boolean permiteed = true;
    int po = 0;


    public void setCandle(String result) throws JSONException {

        List<DataEntry> b = new ArrayList<>();


        JSONArray jA = new JSONArray(result);
        for (int i = 0; i < jA.length(); i++) {
            JSONObject jo = jA.getJSONObject(i);
            b.add(new CustomDataEntry(jo.get("date") + "", Float.parseFloat(jo.get("highest_price") + ""),
                    Float.parseFloat(jo.get("lowest_price") + ""),
                    Float.parseFloat(jo.get("first_price") + ""),
                    Float.parseFloat(jo.get("close_price") + "")));

        }
        AnyChartView anyChartView = root.findViewById(R.id.candle);
        anyChartView.setPadding(15, 15, 15, 15);
        anyChartView.addFont("iransansmedium", "file:///android_asset/iransansmedium.ttf");
        Table table = Table.instantiate("x");

        table.addData(b);


        TableMapping mapping = table.mapAs("{x: 'x',open: 'open', high: 'high', low: 'low', close: 'close'}");

        Stock stock = AnyChart.stock();
        stock.startSelectMarquee(false);


        Plot plot = stock.plot(0);


        stock.tooltip().separator().enabled(false);
        stock.tooltip().title().fontFamily("iransansmedium");
        stock.tooltip().title().fontSize(14);
        stock.tooltip().fontSize(14);

        stock.tooltip().fontFamily("iransansmedium");
        stock.tooltip().titleFormat("{%tickValue}{dateTimeFormat:yy/MM/dd}");
        plot.xAxis().minorLabels().fontFamily("iransansmedium");
        plot.xAxis().minorLabels().fontSize(14);
        plot.xAxis().labels().fontSize(14);

        plot.xAxis().labels().fontFamily("iransansmedium");
        plot.yAxis(0).labels().fontFamily("iransansmedium");
        plot.legend().enabled(false);

        plot.xAxis().labels().format("{%tickValue}{dateTimeFormat:yy/MM/dd}");
        plot.xAxis().minorLabels().format("{%tickValue}{dateTimeFormat:yy/MM/dd}");
        plot.defaultSeriesType("candlestick");
        plot.addSeries(mapping);
        newThread(anyChartView, stock);


    }

    int max, min, high, low, close,final_p;
    double co_b, co_s, real_s, real_b;

    public void ParseJSon(final String data) throws JSONException, NullPointerException {

        new Thread(new Runnable() {


            @Override
            public void run() {
                if(getActivity() != null){

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int n = 0;


                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(data);

                            co_b = Double.parseDouble(obj.getString("cobuyvolume"));
                            co_s = Double.parseDouble(obj.getString("cosellvolume"));
                            real_s = Double.parseDouble(obj.getString("realsellvolume"));
                            real_b = Double.parseDouble(obj.getString("realbuyvolume"));
                            max = Integer.parseInt(obj.getString("max"));
                            min = Integer.parseInt(obj.getString("min"));
                            high = Integer.parseInt(obj.getString("high"));
                            low = Integer.parseInt(obj.getString("low"));
                            close = Integer.parseInt(obj.getString("closeprice").replace(",", ""));
                            final_p = Integer.parseInt(obj.getString("final_price").replace(",", ""));
                            tabHelper(obj);
                            Iterator<String> iter = obj.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                if (key.contains("_")) {
                                    try {

                                        String value = obj.getString(key);
                                        key = fu.getLocale(key);
                                        setText(value, key, n);
                                        n++;

                                    } catch (JSONException e) {

                                    }
                                }


                            }
                        } catch (JSONException |NumberFormatException e) {
                            e.printStackTrace();
                            co_b = 0;
                            co_s = 0;
                        }
                    }

                });
            }}
        }).start();


    }

    void newThread(final AnyChartView c, final Stock p) {
        new Thread(new Runnable() {


            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            c.setChart(p);
                        }

                    });
                }
            }
        }).start();

    }

    private void viewSize() {
        new Thread(new Runnable() {


            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        double a = real_b / (real_b + co_b);
                        double b = real_s / (real_s + co_b);
                        if(real_b == 0 && co_b == 0 && co_s==0 && real_s ==0 ){
                            w2 = 0;
                            v1.setVisibility(View.INVISIBLE);
                        }


                        LinearLayout.LayoutParams cl4 = new LinearLayout.LayoutParams((int) (w2 * (1 - b)), 50);


                        ConstraintLayout.LayoutParams cl3 = new ConstraintLayout.LayoutParams((int) (w2 * (1 - a)) , 50);

                        v1.setLayoutParams(new ConstraintLayout.LayoutParams((int) (w2 * a), 50));
                        v2.setLayoutParams(new LinearLayout.LayoutParams((int) (w2 * b), 50));
                        v3.setLayoutParams(cl3);
                        if( (int) (w2 * (1 - a)) == 0){
                            v3.setVisibility(View.INVISIBLE);
                        }
                        if(  (int) (w2 * (1 - b))== 0) {
                            v4.setVisibility(View.INVISIBLE);
                        }

                        v4.setLayoutParams(cl4);
                    }
                });
            }
        }).start();

    }

    private void tabHelper(final JSONObject jo) throws JSONException {

        new Thread(new Runnable() {


            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        TableHelper th = new TableHelper();
                        int[] views = th.td_ints;
                        String[] key = th.td_keys;
                        for (int i = 0; i < views.length; i++) {
                            TextView tv = root.findViewById(views[i]);
                            String put = null;
                            try {
                                put = jo.getString(key[i]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (put.equals("0")) {
                                put = "-";
                            }
                            tv.setText(put);
                            int l = i + 1;
                            if (i < 18) {
                                if (l > 6) {
                                    l -= 6;
                                }
                                if (l % 6 <= 3 && l % 6 > 0) {
                                    tv.setTextColor(getResources().getColor(R.color.blue));
                                } else {
                                    tv.setTextColor(getResources().getColor(R.color.red));
                                }
                            } else {
                                if (i < 22) {
                                }
                            }


                        }
                        center.setVisibility(View.VISIBLE);
                        dc.setVisibility(View.VISIBLE);
                        uc.setVisibility(View.VISIBLE);
                        ConstraintLayout cl = root.findViewById(R.id.contall);
                        cl.setVisibility(View.VISIBLE);
                        progressConf();
                        loading.cancel();

                    }
                });
            }

        }).start();
    }




    @Override
    public void onPause() {
        super.onPause();



    }

    private void setText(String text, String key, int i) {
        TextView tv;

        tv = (TextView) views[i];
        if (i > 2 && i + 10 < views.length) {
            TextView keym = (TextView) views[i + 10];
            keym.setTextColor(Color.GRAY);
            keym.setText(key);
        }
        if (i < 13) {
            tv.setText(text);

            text = PersianToEnglish(text);

            if (i == 0) {

            }
            if (text.contains("مجاز")) {
                tv.setTextColor(getResources().getColor(R.color.green));
            } else if (text.contains("ممنوع"))
                tv.setTextColor(getResources().getColor(R.color.red));
        }
        if (text.contains("%")) {
            Float percent = Float.parseFloat(text.replace("%", ""));

            if (percent > 0) {
                tv.setTextColor(getResources().getColor(R.color.green));
            } else {
                tv.setTextColor(getResources().getColor(R.color.red));
            }
        }


    }

    public static String PersianToEnglish(String persianStr) {
        String answer = persianStr;
        answer = answer.replace("۱", "1");
        answer = answer.replace("۲", "2");
        answer = answer.replace("۳", "3");
        answer = answer.replace("۴", "4");
        answer = answer.replace("۵", "5");
        answer = answer.replace("۶", "6");
        answer = answer.replace("۷", "7");
        answer = answer.replace("۸", "8");
        answer = answer.replace("۹", "9");
        answer = answer.replace("۰", "0");
        return answer;
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
            if (stringBuffer == null) {
                return "";
            }
            return stringBuffer.toString();


        }

        @Override
        protected void onPostExecute(String result) {

            if (!result.equals("")) {
                try {
                    if (result.contains("[{")) {
                        setCandle(result);

                    } else {
                        ParseJSon(result);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}