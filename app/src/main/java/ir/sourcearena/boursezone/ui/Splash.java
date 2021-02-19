package ir.sourcearena.boursezone.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import id.voela.actrans.AcTrans;
import ir.sourcearena.boursezone.TourtoApp;
import ir.sourcearena.boursezone.tools.GetUser;
import ir.sourcearena.boursezone.tools.NetworkChecker;
import ir.sourcearena.boursezone.Account.Login;
import ir.sourcearena.boursezone.Constants;
import ir.sourcearena.boursezone.MainActivity;
import ir.sourcearena.boursezone.R;

public class Splash extends AwesomeSplash {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    SharedPreferences sp;
    boolean first;

    @Override
    public void initSplash(ConfigSplash configSplash) {

        GetUser gu = new GetUser(getApplicationContext());

        first = gu.getFirst();


        configSplash.setBackgroundColor(R.color.primary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(500); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default
        configSplash.setOriginalHeight(700); //in relation to your svg (path) resource
        configSplash.setOriginalWidth(1200);
        //Customize Logo
        configSplash.setTitleSplash("");

        configSplash.setLogoSplash(R.drawable.logo2); //or any other drawable
        configSplash.setAnimLogoSplashDuration(2000); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)



    }

    @Override
    public void animationsFinished() {
        NetworkChecker nt = new NetworkChecker(Splash.this);
        if(nt.isNetworkAvailable()) {
        if(nt.isServerAvailable()) {
            if(!first){
                startActivity(new Intent(this, TourtoApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }else{
                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
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
                    if(!first){
                        startActivity(new Intent(ctx, TourtoApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }else{
                        startActivity(new Intent(ctx, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                    new AcTrans.Builder(ctx).performFade();

                    ctx = null;
                }

                handler.postDelayed(r, 1000);

            }};
             handler.postDelayed(r, 1000);
    }
}