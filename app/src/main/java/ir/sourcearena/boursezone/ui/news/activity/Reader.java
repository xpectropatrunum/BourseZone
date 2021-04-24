package ir.sourcearena.boursezone.ui.news.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.appbar.AppBarLayout;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.ui.LoadingView;


public class Reader extends AppCompatActivity {

    ImageView im;
    WebView web;
    TextView title, date;
    ConstraintLayout appbar;
    int lastoff , pic= 0;
    int actionBarHeight;
    float alpha = 0;
    LoadingView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences s = getSharedPreferences("per", Context.MODE_PRIVATE);
        s.edit().putBoolean("permitted",false).apply();
        View root = getLayoutInflater().inflate(R.layout.news_reader, null);

        lv = new LoadingView(getLayoutInflater(),root,this);
        setContentView(lv.addLoadingBar(true));
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
             actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        appbar = findViewById(R.id.actionbar);
        appbar.setAlpha(0f);
        DefineViews();

        AppBarLayout mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (pic > 0) {
                    alpha = ((float) (verticalOffset) + (float) (pic)) / (float) (actionBarHeight);
                    alpha = ((-1f) * alpha + 1.5f)*2;


                        appbar.setAlpha(alpha);



                    lastoff = verticalOffset;

                }else if(pic == 0){
                    appbar.setAlpha(1f);
                }
            }
        });

        ActionBar_();
            PUT();


    }
    public void share_new(String text) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        String shareBody = text+ "\n" + "اپلیکیشن " + getResources().getString(R.string.app_name) + " " + getResources().getString(R.string.bazaar_link);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "خبر");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "اشتراک گذاری خبر"));
    }

    private void PUT() {

        new setImage().execute(getIntent().getExtras().getString("news_pic"));

        final String responseString = getIntent().getExtras().getString("news_text");

        web.getSettings().setJavaScriptEnabled(true);
        title.setText(getIntent().getExtras().getString("news_title"));
        date.setText(getIntent().getExtras().getString("news_date"));
        web.loadDataWithBaseURL("", responseString, "text/html; charset=utf-8", "UTF-8", "");

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share_new(getIntent().getExtras().getString("news_title") +"\n"+ Jsoup.parse(responseString).text());
            }
        });

    }

    ImageView share;
    private void ActionBar_() {

        TextView title = findViewById(R.id.app_name);
        ImageView back = findViewById(R.id.imageView3);
        share = findViewById(R.id.share_icon);
        title.setText("اخبار");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animation(view, 10000, Techniques.ZoomIn,false);
                finish();
            }
        });
    }
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
    private void DefineViews() {
        im = findViewById(R.id.news_pic);
        web = (WebView) findViewById(R.id.web2);
        title = findViewById(R.id.news_title_);
        date = findViewById(R.id.reader_date);


    }
    private class setImage extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params) {
            URL link = null;
            try {
                link = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(link.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            final Bitmap finalBitmap = bitmap;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    im.setImageBitmap(finalBitmap);
                    try{
                        pic = finalBitmap.getHeight();
                    }catch (NullPointerException b){

                    }

                    lv.cancelNews();
                }
            });

            return "";


        }

        int p = 0;

        @Override
        protected void onPostExecute(final String result) {



        }
    }





}
