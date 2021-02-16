package ir.sourcearena.boursezone.Account.Purchase;

import ir.sourcearena.boursezone.tools.GetUser;
import ir.sourcearena.boursezone.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;

    List<String[]> titles;

    LayoutInflater inflter;
    GetUser gu;

    public CustomAdapter(Context context, List<String[]> titles) {
        this.context = context;

        this.titles = titles;
        inflter = (LayoutInflater.from(context));
        this.gu = new GetUser(context);
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

        view = inflter.inflate(R.layout.purchase, null);
        view.setFocusable(false);
        view.setClickable(false);
        view.setFocusableInTouchMode(false);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        TextView tt = view.findViewById(R.id.textView41);
        TextView p1 = view.findViewById(R.id.textView36);
        TextView p2 = view.findViewById(R.id.textView37);
        tt.setText(titles.get(i)[0]);
        p1.setText(titles.get(i)[1]+" تومان");
        p2.setText(titles.get(i)[2]+" تومان");
        p1.setPaintFlags(p1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);






        return view;
    }


}