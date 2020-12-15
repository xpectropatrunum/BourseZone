package ir.sourcearena.filterbourse.ui.watcher;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.voela.actrans.AcTrans;
import ir.sourcearena.filterbourse.MainActivity;
import ir.sourcearena.filterbourse.NamadRouter;
import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;
import ir.sourcearena.filterbourse.tools.NetworkChecker;
import ir.sourcearena.filterbourse.tools.ToastMaker;
import ir.sourcearena.filterbourse.ui.LoadingView;
import ir.sourcearena.filterbourse.ui.dialog.adapter;

public class Watchlist extends Fragment {


    LoadingView loading;
    View root, fin;
    PullRefreshLayout ref;
    SharedPreferences sp;
    Handler handler;
    String currentList;
    boolean permitted = true;

    AsyncTask task;


    @Override
    public void onResume() {
        super.onResume();
        permitted = true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.watcher, container, false);
        loading = new LoadingView(inflater, root,getActivity());


        handler = new Handler();
        sp = getActivity().getSharedPreferences("favorite", Context.MODE_PRIVATE);
        currentList = sp.getString("favorite_list", "");

                currentList = sp.getString("favorite_list", "");

                newThread();



        ref = (PullRefreshLayout) root.findViewById(R.id.refresher);
        ref.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        ref.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Request().execute(Settings.JSON_ALL + sp.getString("favorite_list", ""));

            }
        });


        if (currentList.length() < 4) {
            loading.cancel();

        } else {
            fin = loading.addLoadingBar(false);
        }
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {

        }else {
            handler = new Handler();

            final Runnable r = new Runnable() {
                public void run() {
                    if(getActivity() != null) {
                        SharedPreferences s = getActivity().getSharedPreferences("per", Context.MODE_PRIVATE);


                        if (permitted ||  s.getBoolean("per",true))
                        {
                            sp = getActivity().getSharedPreferences("favorite", Context.MODE_PRIVATE);
                            Log.e("per", permitted + "");
                            if (new NetworkChecker(getContext()).isNetworkAvailable()) {
                               task = new Request().execute(Settings.JSON_ALL + sp.getString("favorite_list", ""));
                            }
                            handler.postDelayed(this, 5000);
                        }
                        else {

                        }
                    }
                }
            };

            handler.postDelayed(r, 5000);
        }


        return fin;
    }

    RecyclerAdapter ra;
    List<Utils> utils;
    LinearLayoutManager layoutManager;
    RecyclerView rv;

    public static Watchlist newInstance(String text) {

        Watchlist f = new Watchlist();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
    void newThread() {


        HandlerThread handlerThread = new HandlerThread("HandlerNews2" );
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {


            @Override
            public void run() {


                        task = new Request().execute(Settings.JSON_ALL + sp.getString("favorite_list", ""));


            }
        });


    }
    boolean update = false;


    public void ParseJSon(String res) throws JSONException {

        loading.cancel();

        if(utils != null && utils.size()>0){
           // update =true;

        }

        utils = new ArrayList<>();


        JSONArray ja = new JSONArray(res);
        for (int i = 0; i < ja.length(); i++) {
            JSONObject jo = ja.getJSONObject(i);
            String name = jo.getString("name");


            utils.add(new Utils(jo.getString("name"),
                    jo.getString("full_name"),
                    jo.getString("state"),
                    jo.getString("close_price"),
                    jo.getString("close_price_change"),
                    jo.getString("close_price_change_percent")));

        }

        rv = root.findViewById(R.id.favorite_recycler);
        layoutManager = new LinearLayoutManager(getActivity());
        if(update){
            ra.notifyDataSetChanged();
        }else {
            ra = new RecyclerAdapter(getContext(), utils, new RecyclerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Utils item) {
                    Intent in = new Intent(getActivity(), NamadRouter.class);
                    in.putExtra(Settings.TITLE_EXTRA, item.getName());
                    getActivity().startActivity(in);
                    new AcTrans.Builder(getActivity()).performFade();
                    permitted = false;
                    task.cancel(true);
                    SharedPreferences s = getActivity().getSharedPreferences("per", Context.MODE_PRIVATE);
                    s.edit().putBoolean("permitted",false).apply();

                }
            }, new RecyclerAdapter.OnItemLongClickListener() {
                @Override
                public void onItemLongClick(final Utils items) {

                    DialogPlus dialog = DialogPlus.newDialog(getActivity())
                            .setContentHolder(new adapter(R.layout.dialog_dideban, items.getFName(), getActivity()))
                            .setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(DialogPlus dialog, View view) {


                                    if (view.getId() == R.id.card_delete) {

                                        final SharedPreferences sp = getActivity().getSharedPreferences("favorite", Context.MODE_PRIVATE);
                                        String currentList = sp.getString("favorite_list", "");
                                        sp.edit().putString("favorite_list", currentList.replace("," + items.getName(), "")).apply();
                                        sp.edit().putBoolean(items.getName(), false).apply();
                                        utils.remove(items);

                                        ra.notifyDataSetChanged();
                                        rv.setLayoutManager(layoutManager);
                                        rv.setAdapter(ra);

                                        dialog.dismiss();
                                    }
                                }
                            })
                            .setExpanded(false)
                            .create();
                    dialog.show();


                }
            });

            rv.setLayoutManager(layoutManager);
            rv.setAdapter(ra);
        }



    }

    @Override
    public void onPause() {
        super.onPause();


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        permitted = false;
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
                httpURLConnection.setRequestProperty("Connection", "close");

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

                    ref.setRefreshing(false);
                    ParseJSon(result);


                } catch (JSONException e) {
                    e.printStackTrace();
                    //new ToastMaker(getContext(),Settings.FAIL_RELOAD);
                    Log.e("json exception","watchlist");
                }


            }else{
                //new ToastMaker(getContext(),Settings.FAIL_RELOAD);
                Log.e("res empty","watchlist");
            }
        }
    }


}