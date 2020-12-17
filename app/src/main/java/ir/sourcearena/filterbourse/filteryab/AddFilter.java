package ir.sourcearena.filterbourse.filteryab;

import android.content.Intent;
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
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionInflater;

import com.anychart.AnyChartView;
import com.anychart.charts.Cartesian;
import com.baoyz.widget.PullRefreshLayout;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.labo.kaji.fragmentanimations.MoveAnimation;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.protocol.HTTP;
import id.voela.actrans.AcTrans;
import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.dbhelper.Filter;
import ir.sourcearena.filterbourse.dbhelper.Helper;
import ir.sourcearena.filterbourse.filteryab.adapter.FilterUtils;
import ir.sourcearena.filterbourse.filteryab.adapter.RecyclerAdapter;
import ir.sourcearena.filterbourse.filteryab.advanced.AdvanceAdder;
import ir.sourcearena.filterbourse.filteryab.simple.SimpleAdder;
import ir.sourcearena.filterbourse.filteryab.simple.adapter.dialogAdapter;
import ir.sourcearena.filterbourse.ui.LoadingView;
import ir.sourcearena.filterbourse.ui.watcher.Utils;

public class AddFilter extends Fragment {


    View root;
    Helper db;
    LoadingView loading;
    List<FilterUtils> utils = new ArrayList<>();
    Bundle save;
    PullRefreshLayout ref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.filterpage, container, false);
        loading = new LoadingView(inflater, root, getActivity());
        ref = (PullRefreshLayout) root.findViewById(R.id.refresher);
        ref.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);


        FlotiongActions();
        initiateDB();
        readFromDB();
        List<Filter> filters = db.getAllFilters();


        ref.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readFromDB();
            }
        });
        if (filters.size() == 0) {
            return root;
        }
        return loading.addLoadingBar(false);


    }

    Utils myData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            myData = (Utils) savedInstanceState.getSerializable("list");
            Log.e("saved", "oj");
        } else {
            if (myData != null) {

            } else {

                //myData = computeData();
            }
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        save = outState;
    }

    String conds = "";
    String names = "";

    private void readFromDB() {
        conds = "";
        names = "";
        List<Filter> filters = db.getAllFilters();


        for (int i = 0; i < filters.size(); i++) {

            conds += filters.get(i).getFunctionr() + (i == filters.size() - 1 ? "" : "||");
            names += filters.get(i).getName() + (i == filters.size() - 1 ? "" : ",,,");


        }
        if (filters.size() == 0) {
            loading.cancel();

        }
            newThread(names, conds, "https://sourcearena.ir/androidFilterApi/userfilter/filterlist.php");


    }


    private void initiateDB() {
        db = new Helper(getContext());
    }

    RecyclerAdapter adapter;

    private void RecyclerView() {
        loading.cancel();
        ref.setRefreshing(false);
        RecyclerView rv = root.findViewById(R.id.user_filters);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);

        if (utils.size() > 0) {
            root.findViewById(R.id.no_user_filter_text).setVisibility(View.GONE);
        } else {
            root.findViewById(R.id.no_user_filter_text).setVisibility(View.VISIBLE);
        }
        adapter = new RecyclerAdapter(getContext(), utils, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final FilterUtils item, View im, TextView tv) {

                final int id = utils.indexOf(item);
                if (im.getId() == R.id.imageView5) {


                    DialogPlus dialog = DialogPlus.newDialog(getContext()).setContentHolder(new dialogAdapter(R.layout.dialog_menu_filter, "",getLayoutInflater()))
                           .setGravity(Gravity.CENTER)
                            .setExpanded(false)
                            .setFooter(R.layout.dia_foot).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(DialogPlus dialog, View view) {


                            if (view.getId() == R.id.btn_edit) {
                                Intent in = null;

                                if(db.getType(item.getName()) == 2){
                                    in = new Intent(getContext(),AdvanceAdder.class);
                                }
                                else if(db.getType(item.getName()) == 1){
                                    in = new Intent(getContext(),SimpleAdder.class);
                                }
                                dialog.dismiss();
                                in.putExtra("name",item.getName());
                                in.putExtra("cond",db.getFunction(item.getName()));

                                getActivity().startActivityForResult(in, 0);

                            }
                            else if (view.getId() == R.id.btn_remove) {






                                db.deleteFilter(item.getName());
                                utils.remove(item);

                                adapter.notifyItemRemoved(id);
                                dialog.dismiss();

                            }
                        }
                    })
                            .create();




                    dialog.show();


                } else {


                    Fragment fragment = new showFilter(js.get(id), tv.getText().toString());
                    fragment.setEnterTransition(TransitionInflater.from(getActivity()).inflateTransition(android.R.transition.move));


                    // Add Fragment B
                    FragmentTransaction ft = getFragmentManager().beginTransaction()
                            .replace(R.id.filter_show, fragment);

                    ft.commit();


                }


            }
        });
        loading.cancel();


        rv.setAdapter(adapter);
    }

    public void FlotiongActions() {
        final FloatingActionsMenu fm = root.findViewById(R.id.right_labels);
        AddFloatingActionButton f1 = root.findViewById(R.id.add_simple);
        AddFloatingActionButton f2 = root.findViewById(R.id.add_advanced);
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fm.isExpanded()) {
                    Intent in = new Intent(getActivity(), SimpleAdder.class);
                    startActivityForResult(in, 0);
                }


            }
        });
        f2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { if (fm.isExpanded()) {
                Intent in = new Intent(getActivity(), AdvanceAdder.class);
                startActivityForResult(in, 0);
            }

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.getBooleanExtra("added", false)) {
                loading.reshow();
                if (db.getAllFilters().size() > 0) {
                    utils.clear();

                    names = "";
                    conds = "";


                }


                readFromDB();
            }
        }

    }

    JSONArray jFilter;
    List<JSONArray> js;

    void newThread(final String nameo, final String conds, final String s) {


        HandlerThread handlerThread = new HandlerThread("TesHandlerّFilter");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler handler = new Handler(looper);
        handler.post(new Runnable() {


            @Override
            public void run() {

                new Request().execute(nameo,conds,s);



            }
        });


    }

    private class Request extends AsyncTask<String, Void, String> {

        StringBuffer stringBuffer;

        @Override
        protected String doInBackground(String... params) {
            stringBuffer = null;
            try {


                URL url = new URL(params[2]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json; text/html; charset=utf-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                JSONObject obj = new JSONObject();

                obj.put("token", "fpa");
                obj.put("condition", params[1]);

                DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
                wr.writeBytes(obj.toString());
                wr.flush();


                int code = httpURLConnection.getResponseCode();
                BufferedReader bufferedReader = null;
                if (code == 200) {
                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuffer = new StringBuffer();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }


                }
                wr.close();
                bufferedReader.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
            if (stringBuffer == null) {
                return "";
            }
            return stringBuffer.toString();


        }



        @Override
        protected void onPostExecute(final String result) {


            if (!result.equals("")) {

             

                success(result);


            }
        }


    }

    private void success(String result) {
        utils = new ArrayList<>();
        try {
            js = new ArrayList<>();

            String count, name = "";
            String[] name_ = names.split(",,,");
            JSONArray jA = new JSONArray(result);
            for (int i = 0; i < jA.length(); i++) {

                js.add(jA.getJSONObject(i).getJSONArray("result"));


                count = "تعداد نماد ها: " + jA.getJSONObject(i).getInt("count");
                name = name_[i];
                utils.add(new FilterUtils(name, count));
            }


            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RecyclerView();
                }
            });
        } catch (JSONException s) {

        }
    }


    public static AddFilter newInstance(String text) {
        AddFilter f = new AddFilter();
        Bundle b = new Bundle();
        b.putString("msg", text);
        f.setArguments(b);
        return f;
    }
}