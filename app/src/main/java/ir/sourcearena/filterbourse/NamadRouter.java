package ir.sourcearena.filterbourse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import ir.sourcearena.filterbourse.fundamental.java.Fundamental;
import ir.sourcearena.filterbourse.stockholder.stockholder;
import ir.sourcearena.filterbourse.technical.Technical;
import ir.sourcearena.filterbourse.tools.NetworkChecker;
import ir.sourcearena.filterbourse.tools.ToastMaker;
import ir.sourcearena.filterbourse.ui.LoadingView;
import ir.sourcearena.filterbourse.ui.dialog.adapter;

public class NamadRouter extends AppCompatActivity  {


    BubbleNavigationLinearView bb;
    ViewPager pager;

    @Override
    protected void onPause() {
        super.onPause();

    }
    boolean ok = false;
    boolean permiteed = true;
    @Override
    public void onDestroy() {
        super.onDestroy();
        permiteed = false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.namad_router);



        actionbar();

        pager = (ViewPager) findViewById(R.id.viewPager2);
        final NamadRouter.MyPagerAdapter mp = new MyPagerAdapter(getSupportFragmentManager());
         bb = findViewById(R.id.top_navigation_constraint);

         bb.setTypeface(ResourcesCompat.getFont(this, (R.font.iranyekanmedium)));

        final Handler handler = new Handler();
        final ConstraintLayout cl = findViewById(R.id.network_cont);
        if(!ok) {
            if (new NetworkChecker(NamadRouter.this).isNetworkAvailable()) {

                cl.setVisibility(View.GONE);
                ok = true;
                pager.setAdapter(mp);
            } else {
                cl.setVisibility(View.VISIBLE);
            }
            
        }

            final Runnable r = new Runnable() {
                public void run() {

                    if (!ok) {
                        if (new NetworkChecker(NamadRouter.this).isNetworkAvailable()) {

                            cl.setVisibility(View.GONE);
                            ok = true;
                            pager.setAdapter(mp);
                        } else {
                            cl.setVisibility(View.VISIBLE);
                        }
                        handler.postDelayed(this, 1500);
                    }


                }
            };

            handler.postDelayed(r, 1500);






        int limit = (mp.getCount() > 1 ? mp.getCount() - 1 : 1);
        bb.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                pager.setCurrentItem(4-position,true);
            }
        });




        pager.setOffscreenPageLimit(limit);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                bb.setCurrentActiveItem(4-position);
            }
        });

    }



    boolean state = false;
    int idc;
    Boolean toggle;
    int p = 0;
    int id = R.drawable.ic_round_favorite_border_24;
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
            if (isChangingConfigurations()) {
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

    Settings setting;
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<FilterUtils> utils;


    public void ParseJSon(String data) throws JSONException {

        utils = new ArrayList<>();
        JSONArray jA = new JSONArray(data);

        if (jA.length() == 0) {

        }else{
            nazer.setVisibility(View.VISIBLE);
        }
        for (int i = 0; i < jA.length(); i++) {
            JSONObject obj = jA.getJSONObject(i);
            Log.e("d", obj.getString("time"));
            utils.add(new FilterUtils("", "",
                    obj.getString("head"), obj.getString("time"), obj.getString("text"), "", "", "", "cfn3"));
        }









    }
     ImageView nazer;
    public void actionbar(){
        final SharedPreferences sp = getSharedPreferences("favorite", Context.MODE_PRIVATE);
        final String name = getIntent().getExtras().getString(Settings.TITLE_EXTRA);
        TextView tv = findViewById(R.id.namad_full_name);
        tv.setText(name);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p++%3 == 1){
                    animation(view, 10000, Techniques.Tada,false);
                }

            }
        });
        ImageView back = findViewById(R.id.imageView3);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                YoYo.with(Techniques.ZoomOut)
                        .duration(500)
                        .playOn(view);
                finish();
            }
        });
       nazer = findViewById(R.id.nazer);
        new Request().execute(setting.JSON_INSPECT_TITLES + getIntent().getExtras().getString(Settings.TITLE_EXTRA));

        toggle = sp.getBoolean(name,false);
        final ImageView v = findViewById(R.id.favorite_action);

        nazer.setVisibility(View.GONE);
        nazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPlus dialog = DialogPlus.newDialog(NamadRouter.this)
                        .setContentHolder(new adapter(R.layout.frag_nazer, "", NamadRouter.this))

                        .setExpanded(false)
                        .setGravity(Gravity.CENTER)
                        .create();
                dialog.show();
                RecyclerView rv = dialog.getHolderView().findViewById(R.id.nazer_cycle);
                layoutManager = new LinearLayoutManager(NamadRouter.this) {
                    @Override
                    public boolean canScrollVertically() {

                        return false;
                    }
                };
                mAdapter = new NazerAdapter(NamadRouter.this, utils);
                rv.setLayoutManager(layoutManager);

                rv.setAdapter(mAdapter);

            }
        });



        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialog = DialogPlus.newDialog(NamadRouter.this)
                        .setContentHolder(new adapter(R.layout.dialog_add_watcher, "", NamadRouter.this))
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {


                                if (view.getId() == R.id.card_delete) {

                                }
                            }
                        })
                        .setExpanded(false)
                        .setGravity(Gravity.CENTER)
                        .create();
                dialog.show();

                    YoYo.with(Techniques.Pulse)
                            .duration(1000)
                            .playOn(view);
                final SharedPreferences e = getSharedPreferences("watchers",Context.MODE_PRIVATE);
                String n = e.getString("cat_names","پیشفرض;");

                final String name = getIntent().getExtras().getString(Settings.TITLE_EXTRA);

                final String[] nA = n.split(";");
                    ListView lv = dialog.getHolderView().findViewById(R.id.add_watcher_list);
                    lv.setAdapter(new ArrayAdapter(NamadRouter.this,R.layout.list_add, R.id.watcher_name, nA));
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String n = e.getString("d"+nA[i],"");
                            if(n.contains(name)){
                                new ToastMaker(getBaseContext(), "این نماد در دیده بان مورد نظر موجود است");
                            }else {
                                if (e.edit().putString("d"+nA[i], n + name+ ";" ).commit()) {
                                    new ToastMaker(getBaseContext(), "اضافه شد");
                                    Log.e("dd", nA[i]);

                                } else {
                                    new ToastMaker(getBaseContext(), "خطایی رخ داد");

                                }
                            }
                            dialog.dismiss();
                        }
                    });




            }
        });

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        private FragmentManager fragmentManager;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragmentManager = fm;

        }

        @Override
        public Fragment getItem(int pos) {
            Fragment fragment = fragmentManager.findFragmentByTag("android:switcher:" +
                    pager.getId() + ":" + getItemId(pos));

            if (fragment != null) {
                return fragment;
            } else {
                switch (pos) {
                    case 4:
                        return new Codal();
                    case 3:
                        return new stockholder();
                    case 1:
                        return new Technical();
                    case 2:
                        return new Fundamental();

                    case 0:
                        return new NamadActivity();

                    default:
                        return null;

                }
            }

        }

        @Override
        public int getCount() {
            return 5;
        }
    }
}