package ir.sourcearena.boursezone.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import ir.sourcearena.boursezone.MainActivity;
import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.TourtoApp;
import ir.sourcearena.boursezone.tools.GetUser;
import ir.sourcearena.boursezone.tools.NetworkChecker;

public class Splash extends AppCompatActivity {

    //DO NOT OVERRIDE onCreate()!
    //if you need to start some services do it in initSplash()!
    SharedPreferences sp;
    boolean first;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sp = getSharedPreferences("theme",MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(sp.getBoolean("night",false)? AppCompatDelegate.MODE_NIGHT_YES:AppCompatDelegate.MODE_NIGHT_NO);
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
                startActivity(new Intent(this, TourtoApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ));
            }else{
                startActivity(new Intent(this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }



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
    int stop = 0;
    private void refresh() {
        handler = new Handler();
        ctx = Splash.this;
     
       r = new Runnable() {
            public void run() {
                if(stop == 0){
                NetworkChecker nt = new NetworkChecker(Splash.this);
                if(nt.isNetworkAvailable() && nt.isServerAvailable() && ctx != null) {
                    stop = 1;
                    if(!first){
                        startActivity(new Intent(ctx, TourtoApp.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ));
                    }else{
                        startActivity(new Intent(ctx, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }


                    ctx = null;
                }

                    handler.postDelayed(r, 1000);
                }


            }};
             handler.postDelayed(r, 1000);
    }
}