package ir.sourcearena.filterbourse;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.navigation.NavigationView;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;


import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


import ir.sourcearena.filterbourse.searchHelper.CustomSuggestionsAdapter;
import ir.sourcearena.filterbourse.searchHelper.Namad;
import ir.sourcearena.filterbourse.tools.GetUser;
import ir.sourcearena.filterbourse.ui.HomeFragment.Home;

import ir.sourcearena.filterbourse.ui.FilterFragment.FilterFragment;
import ir.sourcearena.filterbourse.ui.dialog.adapter;
import ir.sourcearena.filterbourse.ui.news.News;
import ir.sourcearena.filterbourse.ui.watcher.Watchlist;
import ir.sourcearena.filterbourse.filteryab.AddFilter;

/*  tsetr */

public class MainActivity extends AppCompatActivity {
    BottomNavigationBar spaceNavigationView;

    CustomSuggestionsAdapter customSuggestionsAdapter;
    ConstraintLayout drawer;
    SharedPreferences sp;
    SharedPreferences.Editor ed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("news", Context.MODE_PRIVATE);
        ed = sp.edit();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        customSuggestionsAdapter = new CustomSuggestionsAdapter(getLayoutInflater(), MainActivity.this);


        drawer = findViewById(R.id.drawer_opener);

        new Request().execute(Settings.JSON_SEARCH);
        actionbar();

        pager_load();
        animation(appname, 10000, Techniques.Tada,true);
        drawer();
    }

    @Override
    protected void onPause() {

        super.onPause();
        SharedPreferences s = getSharedPreferences("per", Context.MODE_PRIVATE);
        s.edit().putBoolean("permitted",false).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences s = getSharedPreferences("per", Context.MODE_PRIVATE);
        s.edit().putBoolean("permitted",true).apply();
    }

    public void hideKeyboard() {
        Activity activity = MainActivity.this;
        InputMethodManager imm = (InputMethodManager) (activity).getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int android = Build.VERSION.SDK_INT;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model) + " " + android;
        } else {
            return capitalize(manufacturer) + " " + model + " " + android;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    DialogPlus dialog;

    GetUser user;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private void drawer() {
        dl = (DrawerLayout) findViewById(R.id.root);

        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();



        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean s = dl.isOpen();
                if (s) {
                    dl.closeDrawers();
                } else {
                    dl.open();
                }
            }
        });
        user = new GetUser(MainActivity.this);

        user.putName("امیر");

        nv = (NavigationView) findViewById(R.id.nv);
        View headerView = nv.getHeaderView(0);
        TextView username = (TextView) headerView.findViewById(R.id.textView3);
        username.setText(user.getName());

        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                dl.closeDrawers();
                switch (id) {
                    case R.id.reportproblem:

                        dialog = DialogPlus.newDialog(MainActivity.this)
                                .setContentHolder(new adapter(R.layout.dialog_report, "", MainActivity.this))
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(DialogPlus dialog, View view) {
                                        EditText ed = findViewById(R.id.bug_ed);


                                        if (view.getId() == R.id.send_report) {
                                            if (ed.getText().toString().equals("")) {
                                                YoYo.with(Techniques.Shake).duration(1000).playOn(ed);
                                            } else {

                                                try {
                                                    new Request().execute(Settings.REPORT_BUG + "?username=" + URLEncoder.encode(user.getUsername(), "UTF-8") + "&text=" + URLEncoder.encode(ed.getText() + getDeviceName(), "UTF-8"));
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }

                                                hideKeyboard();
                                                dialog.dismiss();
                                            }
                                        }
                                    }
                                }).setGravity(Gravity.TOP)
                                .setExpanded(true)
                                .create();
                        dialog.show();

                        break;
                    case R.id.comment:

                        dialog = DialogPlus.newDialog(MainActivity.this)
                                .setContentHolder(new adapter(R.layout.dialog_report, "", MainActivity.this))
                                .setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(DialogPlus dialog, View view) {
                                        EditText ed = findViewById(R.id.bug_ed);


                                        if (view.getId() == R.id.send_report) {
                                            if (ed.getText().toString().equals("")) {
                                                YoYo.with(Techniques.Shake).duration(1000).playOn(ed);
                                            } else {
                                                try {
                                                    new Request().execute(Settings.LEAVE_COMMENT + "?username=" + URLEncoder.encode(user.getUsername(), "UTF-8") + "&text=" + URLEncoder.encode(ed.getText() + getDeviceName(), "UTF-8"));
                                                } catch (UnsupportedEncodingException e) {
                                                    e.printStackTrace();
                                                }


                                                hideKeyboard();
                                                dialog.dismiss();
                                            }
                                        }
                                    }
                                }).setGravity(Gravity.TOP)
                                .setExpanded(true)
                                .create();
                        dialog.show();

                        break;
                    default:
                        return false;
                }


                return true;
            }

        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    public void animation(final View view, final int time, final Techniques t,final boolean repeat) {
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

    ViewPager pager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu, menu);


        return true;
    }
    TextBadgeItem numberBadgeItem;

    private void pager_load() {

        int cc = sp.getInt("cc",20);
        if(cc != 0){
            numberBadgeItem = new TextBadgeItem()
                    .setBorderWidth(4)
                    .setBorderColorResource(R.color.red)
                    .setBackgroundColorResource(R.color.red)
                    .setText(cc+"")
                    .setHideOnSelect(true);
        }

        spaceNavigationView = findViewById(R.id.bottom_menu);
        pager = (ViewPager) findViewById(R.id.viewPager);
        final MyPagerAdapter mp = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mp);
        pager.setOffscreenPageLimit(5);
        spaceNavigationView
                .addItem(new BottomNavigationItem(R.drawable.eye_50, "دیده بان"))
                .addItem(new BottomNavigationItem(R.drawable.whatshot_50, "فیلتر داغ"))
                .addItem(new BottomNavigationItem(R.drawable.analysis_50, "فیلتریاب"))
                .addItem(new BottomNavigationItem(R.drawable.news_24, "اخبار").setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "خانه"))
                .setBarBackgroundColor(R.color.secondary)




                .setFirstSelectedPosition(0).initialise();



        spaceNavigationView.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                pager.setCurrentItem(4 - position);
                if(position == 3){
                    ed.putInt("cc",0);
                    ed.apply();
                    numberBadgeItem = null;
                }
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });


        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {


                spaceNavigationView.selectTab(4-position);
                //spaceNavigationView.getSelectedIndex().setSelectedIndex(spaceNavigationView.getMenuItemId((4 - position));

                switch (position){
                    case 4:
                        appname.setText("دیده بان");
                        animation(appname,500,Techniques.FadeIn,false);
                        break;
                    case 3:
                        appname.setText("فیلترها");
                        animation(appname,500,Techniques.FadeIn,false);
                        break;
                    case 2:
                        appname.setText("فیلتر کاربر");
                        animation(appname,500,Techniques.FadeIn,false);
                        break;
                    case 1:
                        appname.setText("اخبار بورس");
                        animation(appname,500,Techniques.FadeIn,false);

                        break;
                    case 0:
                        appname.setText("بورس در یک نگاه");
                        animation(appname,500,Techniques.FadeIn,false);
                        break;
                }



            }
        });

    }

    TextView appname;

    public void doSearch(String s) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i)[0].contains(s)) {
                suggestions.add(
                        new Namad(
                                array.get(i)[0],
                                array.get(i)[1],
                                array.get(i)[2])
                );
            }
        }
        customSuggestionsAdapter.notifyDataSetChanged();


    }


    MaterialSearchBar searchBar;

    private void actionbar() {
        suggestions = new ArrayList<>();



        appname = findViewById(R.id.app_name);
        appname.setText("بورس در یک نگاه");
        searchBar = findViewById(R.id.searchBar);


        searchBar.setSpeechMode(false);
        if (customSuggestionsAdapter != null) {
            searchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);
        }
        searchBar.clearAnimation();
        searchBar.setAnimation(null);

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {

                String search = editable.toString();
                searchBar.clearSuggestions();

                customSuggestionsAdapter.setSuggestions(suggestions);
                if (search.length() > 2) {


                    doSearch(search);


                    searchBar.showSuggestionsList();

                }


                if (search.length() == 0) {

                    searchBar.clearSuggestions();
                }
            }
        });

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (enabled) {
                    drawer.setVisibility(View.INVISIBLE);
                } else {
                    drawer.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

            }

            @Override
            public void onButtonClicked(int buttonCode) {
                switch (buttonCode) {
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        //drawer.openDrawer(Gravity.LEFT);
                        break;
                    case MaterialSearchBar.BUTTON_BACK:
                        searchBar.hideSuggestionsList();
                }
            }
        });


    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {

            switch (pos) {

                case 4:

                    return Watchlist.newInstance("ThirdFragment, Instance 4");

                case 3:
                    return FilterFragment.newInstance("FirstFragment, Instance 3");
                case 2:
                    return AddFilter.newInstance("ThirdFragment, Instance 2");
                case 1:
                    return News.newInstance("ThirdFragment, Instance 1");

                default:
                    return Home.newInstance("ThirdFragment, Default");
            }
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      
        Log.e("e",resultCode+"");
        if(requestCode == 10){
            searchBar.closeSearch();
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "برای خروج دوباره دکمه بازگشت را بزنید", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 3000);
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

            if (result.equals("OK")) {
                Toast.makeText(getBaseContext(), "با موفقیت ارسال شد بزودی شاهد تغییرات در اپ باشید :)", Toast.LENGTH_LONG).show();
            }

            try {
                JSONArray ja = new JSONArray(result);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);
                    // suggestions.add(new Namad(jo.getString("n"),jo.getString("f"),jo.getString("m")));
                    //customSuggestionsAdapter.addSuggestion(new Namad(jo.getString("n"),jo.getString("f"),jo.getString("m")));
                    array.add(new String[]{jo.getString("n"), jo.getString("f"), jo.getString("m")});

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private List<Namad> suggestions = new ArrayList<>();
    List<String[]> array = new ArrayList<>();


}