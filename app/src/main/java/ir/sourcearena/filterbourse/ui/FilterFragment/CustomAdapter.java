package ir.sourcearena.filterbourse.ui.FilterFragment;

import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;
import mehdi.sakout.fancybuttons.FancyButton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;

    List<String> titles;

    LayoutInflater inflter;

    public CustomAdapter(Context context, List<String> titles) {
        this.context = context;

        this.titles = titles;
        inflter = (LayoutInflater.from(context));
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

        fb.setCustomTextFont(R.font.iranyekanmedium);
        fb.setText(titles.get(i));


        return view;
    }


}