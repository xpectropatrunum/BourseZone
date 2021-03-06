package ir.sourcearena.boursezone.ui.news;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.baoyz.widget.PullRefreshLayout;
import com.github.ybq.android.spinkit.SpinKitView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ir.sourcearena.boursezone.ui.news.activity.Reader;
import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.ui.LoadingView;

public class News extends Fragment {



    SpinKitView scrollLoading;

    PullRefreshLayout ref;
    ListView lv ;
    LoadingView loading;
    View root;
    int page = 1;
    String bg = "";
    String text = "";
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        sp = getActivity().getSharedPreferences("theme",0);
        bg = !sp.getBoolean("night",false) ? "ffffff":"222222";
        text = !sp.getBoolean("night",false) ? "252A34":"888886";



        sp = getActivity().getSharedPreferences("news", Context.MODE_PRIVATE);
        ed = sp.edit();
        utils = new ArrayList<>();
        root = inflater.inflate(R.layout.a_page_news, container, false);
        loading = new LoadingView(inflater,root,getActivity());
        lv = root.findViewById(R.id.news_List);
        ref = root.findViewById(R.id.refresher);
        ref.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        scrollLoading = root.findViewById(R.id.spin_kit__);
        new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Settings.JSON_NEWS+1+"&bg="+bg+"&text="+text);




        ref.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                utils = new ArrayList<>();
                new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Settings.JSON_NEWS+1+"&bg="+bg+"&text="+text);
            }
        });

        return loading.addLoadingBar(false);


    }



    public static News newInstance(String text) {

        News f = new News();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }


    ListAdapter cs;
    int c = 0;
    private int preLast;

    List<Utils> utils;

        public void ParseJSon(String data) throws JSONException {




            ref.setRefreshing(false);

            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                if(arr.getJSONObject(i).getString("title").equals(sp.getString("last",""))){
                    ed.putInt("cc",i);
                    ed.apply();
                }
                utils.add(new Utils(arr.getJSONObject(i).getString("title"),
                        arr.getJSONObject(i).getString("text")
                ,
                        arr.getJSONObject(i).getString("thumb"),
                        arr.getJSONObject(i).getString("date"),null,   arr.getJSONObject(i).getString("link"),null,  arr.getJSONObject(i).getString("pic")
                        ));
                if(cs != null){
                    cs.notifyDataSetChanged();

                }


            }
            if(page == 1) {
                cs = new ListAdapter(getContext(),utils);

                lv.setAdapter(cs);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Utils item = utils.get(i);

                        Intent in = new Intent(getActivity(), Reader.class);
                        in.putExtra("news_text", item.getItem(1));
                        in.putExtra("news_date", item.getItem(3));
                        in.putExtra("news_pic", item.getPic());
                        in.putExtra("news_title", item.getItem(0));
                        startActivity(in);



                    }
                });

                ref.setRefreshing(false);
                lv.setOnScrollListener(new AbsListView.OnScrollListener() {
                                           @Override
                                           public void onScrollStateChanged(AbsListView absListView, int i) {

                                           }

                                           @Override
                                           public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                               switch(absListView.getId())
                                               {
                                                   case R.id.news_List:

                                                       final int lastItem = firstVisibleItem + visibleItemCount;

                                                       if(lastItem == totalItemCount)
                                                       {
                                                           if(preLast!=lastItem)
                                                           {



                                                               if(page  == utils.size()/19){
                                                                   page++;
                                                                   scrollLoading.setVisibility(View.VISIBLE);

                                                                   new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Settings.JSON_NEWS+page+"&bg="+bg+"&text="+text);
                                                               }

                                                           }
                                                       }
                                               }
                                           }
                                       }
                );
               /* lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (!recyclerView.canScrollVertically(1)) {
                            new Request().execute(Settings.JSON_NEWS + page++);
                            Log.e("e",Settings.JSON_NEWS + page++);

                        }
                    }
                });*/
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
            loading.cancel();
            if(scrollLoading.getVisibility() == View.VISIBLE){
                scrollLoading.setVisibility(View.GONE);

            }
            if (!result.equals("")) {

                try {
                    ParseJSon(result);
                } catch (JSONException | NullPointerException d) {
                    d.printStackTrace();

                    ref.setRefreshing(false);
                    try {
                        Toast.makeText(getContext(), "خطا در بارگذاری اخبار", Toast.LENGTH_LONG).show();
                    } catch (NullPointerException u ) {

                    }
                }


            }
        }
    }



}