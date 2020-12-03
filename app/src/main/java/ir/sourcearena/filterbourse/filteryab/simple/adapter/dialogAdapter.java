package ir.sourcearena.filterbourse.filteryab.simple.adapter;



import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ir.sourcearena.filterbourse.R;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.zip.Inflater;

import ir.sourcearena.filterbourse.Settings;
import ir.sourcearena.filterbourse.ui.watcher.RecyclerAdapter;


public class dialogAdapter extends ViewHolder {

    private Context context;
    private String utils;
    Settings setting;


    public dialogAdapter(int viewResourceId, final String text, final LayoutInflater inf) {
        super(viewResourceId);

        /*this.getInflatedView().findViewById(R.id.remove_watchlist_dia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/
    }



}