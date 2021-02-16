package ir.sourcearena.boursezone.technical;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
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

import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.technical.helper.RecyclerAdapter;
import ir.sourcearena.boursezone.technical.helper.RecyclerAdapter2;
import ir.sourcearena.boursezone.technical.helper.Utils;
import ir.sourcearena.boursezone.technical.helper.Utils2;
import ir.sourcearena.boursezone.ui.LoadingView;
import ir.sourcearena.boursezone.R;
import mehdi.sakout.fancybuttons.FancyButton;

public class Technical extends Fragment {
    View root;

    EditText e1,e2;
    Settings setting;
    CardView cd1 ;
    LoadingView loading;
    RecyclerView rv1,rv2;
    List<Utils> utils;
    List<Utils2> utils2;
    boolean showmore = false;
    FancyButton more;
    ImageView pointer;
    TextView bt,nt,st, active_patern;
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.technical_signal, container, false);
        loading = new LoadingView(inflater,root,getActivity());

        String append = getActivity().getIntent().getExtras().getString(Settings.TITLE_EXTRA, "");
        new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Settings.JSON_TECHNICAL+append);



        return loading.addLoadingBar(false);
    }

    private void firstCard(int b, int n, int s) {


        bt = root.findViewById(R.id.buy_count);
        nt = root.findViewById(R.id.neu_count);
        st = root.findViewById(R.id.sell_count);

        bt.setText(b+"");
        nt.setText(n+"");
        st.setText(s+"");

        float p = (((float)b - (float)s)/((float)(b+n+s)))*90f;
        pointer = root.findViewById(R.id.imageView8);
        Animation animation = new RotateAnimation(0.0f, p,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                1f);

        animation.setDuration(2000);
        animation.setFillAfter(true);
        pointer.startAnimation(animation);

    }
    RecyclerAdapter ra;
    RecyclerAdapter2 ra2;
    private void secondCard(final List<Utils> utils, String s) {


        ra = new RecyclerAdapter(getContext(),utils);
        st = root.findViewById(R.id.textView32);
        rv1 = root.findViewById(R.id.paterns);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {

                return false;
            }
        };
        rv1.setLayoutManager(layoutManager);
        st.setText(s);
        rv1.setAdapter(ra);
        more = root.findViewById(R.id.show_more);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!isExpanded){
                        showMore();
                    }else{
                        showLess();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void showLess()  throws JSONException{
        isExpanded = false;
        more.setText("نمایش همه");

        utils = new ArrayList<>();
        LinearLayoutManager layoutManager =  new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        rv1.setLayoutManager(layoutManager);
        JSONObject jo = new JSONObject(data);
        JSONArray ja = jo.getJSONArray("paterns");
        for(int i =0; i< 5; i++){
            utils.add(new Utils(ja.getJSONObject(i).getString("name"),ja.getJSONObject(i).getBoolean("value")));
        }
        ra = new RecyclerAdapter(getContext(),utils);
        rv1.setAdapter(ra);
    }

    private void thirdCard(List<Utils2> utils2) {


        rv2 = root.findViewById(R.id.indicators);
        ra2 = new RecyclerAdapter2(getContext(),utils2);


        LinearLayoutManager layoutManager =  new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rv2.setLayoutManager(layoutManager);

        rv2.setAdapter(ra2);
        loading.cancel();


    }
    boolean isExpanded = false;
    private void showMore() throws JSONException{
        isExpanded = true;
        more.setText("بستن");

        utils = new ArrayList<>();
        LinearLayoutManager layoutManager =  new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };
        rv1.setLayoutManager(layoutManager);
        JSONObject jo = new JSONObject(data);
        JSONArray ja = jo.getJSONArray("paterns");
        for(int i =0; i< ja.length(); i++){
            utils.add(new Utils(ja.getJSONObject(i).getString("name"),ja.getJSONObject(i).getBoolean("value")));
        }
        ra = new RecyclerAdapter(getContext(),utils);
        rv1.setAdapter(ra);
    }

    @Override
    public void onPause() {
        super.onPause();

        if(task != null)task.cancel(true);
    }
    int n = -1;
    public static Technical newInstance() {

        Technical f = new Technical();
        return f;
    }
    String data = "";
    public void ParseJSon(String data) throws JSONException {
        this.data = data;
        utils = new ArrayList<>();
        utils2 = new ArrayList<>();
        int buy,neu,sell = 0 ;
        String exs = "الگوهای موجود: ";
        JSONObject jo = new JSONObject(data);
        buy = jo.getJSONObject("all").getJSONObject("technical_sum").getInt("buy");
        neu = jo.getJSONObject("all").getJSONObject("technical_sum").getInt("neutral");
        sell = jo.getJSONObject("all").getJSONObject("technical_sum").getInt("sell");
        exs += jo.getString("exists");
        firstCard(buy,neu,sell);

        JSONArray ja = jo.getJSONArray("paterns");
        for(int i =0; i< 5; i++){
            utils.add(new Utils(ja.getJSONObject(i).getString("name"),ja.getJSONObject(i).getBoolean("value")));
        }
        secondCard(utils,exs);

        JSONObject jv = jo.getJSONObject("all");

        Iterator<String> iter = jv.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            String value = jv.getString(key);
            if(!key.equals("technical_sum")) {
                n++;
                utils2.add(new Utils2(ind_names[n], value));
            }





        }
        thirdCard(utils2);

        loading.cancel();


    }
    String[] ind_names = new String[]{
            "RSI(14)",
            "MFI(14)",
            "CCI(14)",
            "WR(14)",
            "SO(14)",
            "MACD(26)",
            "STOCH_RSI(14)",
            "Ichimoku(9_26_52_26)",
            "BB(20)",
            "EMA(5)",
            "EMA(10)",
            "EMA(20)",
            "EMA(30)",
            "EMA(50)",
            "EMA(100)",
            "SMA(5)",
            "SMA(10)",
            "SMA(20)",
            "SMA(30)",
            "SMA(50)",
            "SMA(100)",
            "RV(90)",
          

    };

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
            if(getActivity() != null) {
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
    }



    AsyncTask task;


}
