package ir.sourcearena.boursezone.ui.dialog;



import android.content.Context;
import android.widget.TextView;

import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import ir.sourcearena.boursezone.Settings;


public class adapter extends ViewHolder {

    private Context context;
    private String utils;
    Settings setting;


    public adapter(int viewResourceId, final String text, final Context ctx) {
        super(viewResourceId);
        /*this.getInflatedView().findViewById(R.id.remove_watchlist_dia).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });*/
    }

}