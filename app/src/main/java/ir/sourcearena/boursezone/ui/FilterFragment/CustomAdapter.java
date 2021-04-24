package ir.sourcearena.boursezone.ui.FilterFragment;

import ir.sourcearena.boursezone.tools.GetUser;
import ir.sourcearena.boursezone.R;
import mehdi.sakout.fancybuttons.FancyButton;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.res.ResourcesCompat;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;

    List<String> titles;

    LayoutInflater inflter;
    GetUser gu;
    int max_;

    public CustomAdapter(Context context, List<String> titles,int max_) {
        this.context = context;

        this.titles = titles;
        if(context != null){
            inflter = (LayoutInflater.from(context));

        }
        this.gu = new GetUser(context);
        this.max_ = max_;
    }


    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflter.inflate(R.layout.activity_gridview, null);
        view.setFocusable(false);
        view.setClickable(false);
        view.setFocusableInTouchMode(false);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        FancyButton fb = view.findViewById(R.id.btn_preview);

        int grey = ResourcesCompat.getColor(context.getResources(),R.color.grey,null);
        fb.setCustomTextFont(R.font.iranyekanmedium);
        if(gu.isPremium()){
            fb.setText(titles.get(i));
        }else{
            if(i < max_){
                fb.setText(titles.get(i));
            }else{
                fb.setText(titles.get(i)+" \uD83D\uDD12");
                fb.setTextColor(grey);
                fb.setBorderColor(grey);
                fb.setFocusBackgroundColor(grey);
            }
        }




        return view;
    }


}