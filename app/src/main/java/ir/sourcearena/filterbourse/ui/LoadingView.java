package ir.sourcearena.filterbourse.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.zip.Inflater;

import ir.sourcearena.filterbourse.R;

public class LoadingView {
    LayoutInflater inflater;

    View root;
    View customView;
    Activity g;
    CoordinatorLayout cl2;
    ConstraintLayout cl;
    public LoadingView(LayoutInflater inflater,View root,Activity g) {

        this.inflater = inflater;
        this.root = root;
        this.g = g;
    }



    public View addLoadingBar(final Boolean f) {
        customView = inflater.inflate(R.layout.loadingview,null);
        if(f){
            cl2 = root.findViewById(R.id.base_);
            customView.setLayoutParams(new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }else {
            cl = (ConstraintLayout) root.findViewById(R.id.base);
            customView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }


        new Thread(new Runnable() {


            @Override
            public void run() {

                g.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(f){
                            cl2.addView(customView);
                        }else {
                            cl.addView(customView);
                        }

                    }
                });
            }

        }).start();


        if(!f) {
            return cl;
        }
            return cl2;


    }
    public View cancel(){
        if(cl != null) {
            customView.setVisibility(View.GONE);
        }

        return null;

    }
     public View cancelNews(){
        if(cl2 != null) {
            customView.setVisibility(View.GONE);
        }

        return null;

    }
    public View reshow(){
        if(cl != null) {
            customView.setVisibility(View.VISIBLE);
        }

        return null;

    }
}
