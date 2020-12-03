package ir.sourcearena.filterbourse.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import id.voela.actrans.AcTrans;
import ir.sourcearena.filterbourse.Constants;
import ir.sourcearena.filterbourse.MainActivity;
import ir.sourcearena.filterbourse.R;

import ir.sourcearena.filterbourse.tools.NetworkChecker;

public class Splash extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!


    @Override
    public void initSplash(ConfigSplash configSplash) {

        configSplash.setBackgroundColor(R.color.primary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher); //or any other drawable
        configSplash.setAnimLogoSplashDuration(500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)


        //Customize Path
        configSplash.setPathSplash(Constants.DROID_LOGO); //set path String
        configSplash.setOriginalHeight(4); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(4); //in relation to your svg (path) resource
        configSplash.setAnimPathStrokeDrawingDuration(5);
        configSplash.setPathSplashStrokeSize(3); //I advise value be <5
        configSplash.setPathSplashStrokeColor(R.color.primary); //any color you want form colors.xml
        configSplash.setAnimPathFillingDuration(5);
        configSplash.setPathSplashFillColor(R.color.pureWhite); //path object filling color


        //Customize Title
        configSplash.setTitleSplash("برنامه من");
        configSplash.setTitleTextColor(R.color.pureWhite);
        configSplash.setTitleTextSize(23f);
        configSplash.setAnimTitleDuration(15);
        configSplash.setAnimTitleTechnique(Techniques.FlipInX);
        configSplash.setTitleFont("iranyekanbold.ttf"); //provide string to your font located in assets/fonts/

    }

    @Override
    public void animationsFinished() {
        NetworkChecker nt = new NetworkChecker(Splash.this);
        if(nt.isNetworkAvailable()) {
        if(nt.isServerAvailable()) {

            startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            new AcTrans.Builder(this).performFade();
        }else{
            refresh();
            Toast.makeText(getApplicationContext(),"خطا در اتصال به سرور لطفا بعدا تلاش کنید",Toast.LENGTH_LONG).show();

        }
        }else{
            refresh();
            Toast.makeText(getApplicationContext(),"اتصال اینترنت خود را چک کنید",Toast.LENGTH_LONG).show();
        }
    }
    Handler handler;
    Runnable r;

    @Override
    protected void onPause() {
        super.onPause();
    }

    Context ctx;
    private void refresh() {
        handler = new Handler();
        ctx = Splash.this;
     
       r = new Runnable() {
            public void run() {
                NetworkChecker nt = new NetworkChecker(Splash.this);
                if(nt.isNetworkAvailable() && nt.isServerAvailable() && ctx != null) {
                    handler.removeCallbacks(r);
                    startActivity(new Intent(Splash.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_NEW_TASK));
                    new AcTrans.Builder(ctx).performFade();
                    ctx = null;
                }

                handler.postDelayed(r, 1000);

            }};
             handler.postDelayed(r, 1000);
    }
}