package ir.sourcearena.boursezone.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import org.json.JSONException;

import id.voela.actrans.AcTrans;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.TourtoApp;
import ir.sourcearena.boursezone.tools.GetUser;
import ir.sourcearena.boursezone.tools.NetworkChecker;
import ir.sourcearena.boursezone.Account.Login;
import ir.sourcearena.boursezone.Constants;
import ir.sourcearena.boursezone.MainActivity;
import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.ui.watcher.Watchlist;

public class Splash extends AppCompatActivity {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    SharedPreferences sp;
    boolean first;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);


        GetUser gu = new GetUser(getApplicationContext());

        first = gu.getFirst();

        handler = new Handler();


        final Runnable r = new Runnable() {
            public void run() {
                animationsFinished();

            }

        };
        handler.postDelayed(r, 2500);




    }

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
            TextView msg = findViewById(R.id.textView46);
            msg.setText("در صورت استفاد از vpn آن را خاموش کنید");

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