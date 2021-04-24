package ir.sourcearena.boursezone;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

public class FieldsUtil {
    private final Context ctx;
    String input;
    String[][] out;
    public  FieldsUtil(Context ctx){
        this.ctx = ctx;
        SharedPreferences sp = ctx.getSharedPreferences("fields",0);
        input = sp.getString("fields","");



        try {
            JSONArray ja = new JSONArray(input);
            out = new String[ja.length()][];
            for(int i = 0; i < ja.length(); i++){
                out[i] = new String[]{ja.getJSONArray(i).getString(0),ja.getJSONArray(i).getString(1)};
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    public String getLocale(String tag){
        for(int i =0; i<out.length; i++){
            if(out[i][0].equals(tag)){
                return out[i][1];
            }
        }
        return null;

    }
}
