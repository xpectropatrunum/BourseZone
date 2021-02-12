package ir.sourcearena.filterbourse.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class GetUser {
    Context ctx;
    SharedPreferences s;
    SharedPreferences.Editor ed;
    public GetUser(Context ctx){
        this.ctx = ctx;
        s = ctx.getSharedPreferences("user",Context.MODE_PRIVATE);
        ed = s.edit();

    }
    public String getUsername(){
        return s.getString("username","");
    }
    public boolean isPremium(){
        return s.getBoolean("premium",false);
    }
    public String getName(){
        return s.getString("name","مهمان");
    }
    public String getNumber(){
        return s.getString("number","");
    }
    public void putUsername(String a){
        ed.putString("username",a).commit();
    }
    public void putPremium(boolean premium){ ed.putBoolean("premium",premium).commit(); }
    public void putName(String a){
        ed.putString("name",a).commit();
    }
    public void putNumber(String a){
        ed.putString("number",a).commit();
    }

    public void putTime(Float f) {
        ed.putFloat("time",f).commit();
    }

    public Float getTime() {
        return s.getFloat("time",0f);
    }
    public void putFirst() {
        ed.putBoolean("first",true).commit();
    }

    public boolean getFirst() {
        return s.getBoolean("first",false);
    }
}
