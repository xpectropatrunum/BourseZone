package ir.sourcearena.filterbourse;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import androidx.viewpager.widget.ViewPager;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;


import java.util.Calendar;

import ir.sourcearena.filterbourse.fundamental.java.Fundamental;
import ir.sourcearena.filterbourse.stockholder.stockholder;
import ir.sourcearena.filterbourse.technical.Technical;
import ir.sourcearena.filterbourse.tools.NetworkChecker;

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



        toggle = sp.getBoolean(name,false);
        final ImageView v = findViewById(R.id.favorite_action);

        if(toggle){
            id = R.drawable.ic_baseline_favorite_24;
            idc = R.drawable.ic_round_favorite_border_24;
            state = false;

        }else{
            id = R.drawable.ic_round_favorite_border_24;
            idc = R.drawable.ic_baseline_favorite_24;
            state = true;
        }

        v.setImageDrawable(ResourcesCompat.getDrawable(getResources(),id,null));
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putBoolean(name,state).apply();
                toggle = sp.getBoolean(name,false);
                if(toggle){
                    String currentList = sp.getString("favorite_list","");
                    sp.edit().putString("favorite_list",currentList+","+name).apply();
                    id = R.drawable.ic_baseline_favorite_24;
                    idc = R.drawable.ic_round_favorite_border_24;
                    state = false;

                }else{
                    String currentList = sp.getString("favorite_list","");

                    id = R.drawable.ic_round_favorite_border_24;
                    idc = R.drawable.ic_baseline_favorite_24;
                    sp.edit().putString("favorite_list",currentList.replace(","+name,"")).apply();
                    state = true;
                }

                    YoYo.with(Techniques.Pulse)
                            .duration(1000)
                            .playOn(view);

                v.setImageDrawable(ResourcesCompat.getDrawable(getResources(),id,null));


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
                        return new Nazer();
                    case 3:
                        return new Codal();
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