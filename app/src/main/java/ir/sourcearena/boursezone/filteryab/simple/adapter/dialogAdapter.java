package ir.sourcearena.boursezone.filteryab.simple.adapter;



import android.content.Context;
import android.view.LayoutInflater;

import ir.sourcearena.boursezone.Settings;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;


public class dialogAdapter extends ViewHolder {

    private Context context;
    private String utils;
    Settings setting;
    DialogPlus dp;



    public dialogAdapter( int viewResourceId, final String text, final LayoutInflater inf) {
        super(viewResourceId);

        this.dp =dp;




    }



}