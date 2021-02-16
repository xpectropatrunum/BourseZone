
package ir.sourcearena.boursezone.ui.watcher;

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
import ir.sourcearena.boursezone.tools.NetworkChecker;
import ir.sourcearena.boursezone.NamadRouter;
import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.filteryab.simple.adapter.dialogAdapter;
import ir.sourcearena.boursezone.ui.LoadingView;

public class specificWatcher extends Fragment {


    LoadingView loading;
    View root, fin;
    PullRefreshLayout ref;
    SharedPreferences sp;
    Handler handler;
    String currentList;
    boolean loaded = false ;
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
                    if(loaded){
                        if(getActivity() != null) {
                            SharedPreferences s = getActivity().getSharedPreferences("per", Context.MODE_PRIVATE);


                            if (s.getBoolean("permitted",true))
                            {
                                sp = getActivity().getSharedPreferences("favorite", Context.MODE_PRIVATE);
                                Log.e("per", permitted + "");
                                if (new NetworkChecker(getContext()).isNetworkAvailable()) {
                                    task = new Request().execute(Settings.JSON_ALL + sp.getString("favorite_list", ""));
                                }

                            }
                            else {
                            }
                        }
                    }
                    handler.postDelayed(this, 5000);
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
    List<Utils> old;


    public void ParseJSon(String res) throws JSONException {

        loading.cancel();

        old = utils;

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

        ra = new RecyclerAdapter(getContext(), old,utils, new RecyclerAdapter.OnItemClickListener() {
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

                DialogPlus dialog = DialogPlus.newDialog(getContext()).setContentHolder(new dialogAdapter(R.layout.dialog_menu_filter, "",getLayoutInflater()))
                        .setGravity(Gravity.CENTER)
                        .setExpanded(false)
                        .setFooter(R.layout.dia_foot).setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {


                                if (view.getId() == R.id.btn_edit) {
                                    Intent in = null;

                                    dialog.dismiss();

                                    getActivity().startActivityForResult(in, 0);

                                }
                                else if (view.getId() == R.id.btn_remove) {



                                    final SharedPreferences sp = getActivity().getSharedPreferences("favorite", Context.MODE_PRIVATE);
                                    String currentList = sp.getString("favorite_list", "");
                                    sp.edit().putString("favorite_list", currentList.replace("," + items.getName(), "")).apply();
                                    sp.edit().putBoolean(items.getName(), false).apply();
                                    utils.remove(items);

                                    ra.notifyItemRemoved(utils.indexOf(items));


                                    dialog.dismiss();

                                }
                            }
                        })
                        .create();




                dialog.show();


            }
        });

        rv.setLayoutManager(layoutManager);
        rv.setAdapter(ra);
        loaded = true;





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