package ir.sourcearena.boursezone.tools;

import android.content.Context;
import android.widget.Toast;

public class ToastMaker {
    public ToastMaker(Context ctx,String text){
        Toast.makeText(ctx,text,Toast.LENGTH_LONG).show();
    }
}
