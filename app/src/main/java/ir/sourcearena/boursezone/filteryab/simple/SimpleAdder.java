package ir.sourcearena.boursezone.filteryab.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.ybq.android.spinkit.SpinKitView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import ir.sourcearena.boursezone.dbhelper.Filter;
import ir.sourcearena.boursezone.filteryab.simple.adapter.AddSegments;
import ir.sourcearena.boursezone.filteryab.simple.adapter.FilterFieldHelper;
import ir.sourcearena.boursezone.filteryab.simple.adapter.RecyclerAdapter;
import ir.sourcearena.boursezone.filteryab.simple.adapter.dialogAdapter;
import ir.sourcearena.boursezone.tools.ToastMaker;
import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.dbhelper.Helper;
import mehdi.sakout.fancybuttons.FancyButton;

public class SimpleAdder extends AppCompatActivity {
    RecyclerAdapter adapter;
    FancyButton addFilter, submit;
    RecyclerView rv;
    EditText ed;
    Helper db;
    int EDITMODE = 0;
    List<AddSegments> utils;
    SpinKitView loading ;
    List<String> sp1,sp2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsimplefilter);
        loading = findViewById(R.id.spin_kit);
        if(getIntent().getExtras() != null){
            EDITMODE = 1;
        }
        declareButton();
        utils = new ArrayList<>();
        if(getIntent().getExtras() != null){
            ed.setEnabled(false);
            loadSaved(getIntent().getExtras().getString("name"),getIntent().getExtras().getString("cond"));
        }


        initiateDB();
        setTileActionbar();
        list();





        declareRecycler();
        Submit();
    }

    private void list() {
        sp1 = new ArrayList<>();
        sp1.add("تعداد معاملات");
        sp1.add("حجم معاملات");
        sp1.add("ارزش معاملات");
        sp1.add("قیمت دیروز");
        sp1.add("اولین قیمت");
        sp1.add("کمترین قیمت");
        sp1.add("بیشترین قیمت");
        sp1.add("آخرین قیمت");
        sp1.add("تغییر آخرین قیمت");
        sp1.add("درصد تغییر آخرین قیمت");
        sp1.add("قیمت پایانی");
        sp1.add("تغییر قیمت پایانی");
        sp1.add("درصد تغییر قیمت پایانی");
        sp1.add("eps");
        sp1.add("p/e");
        sp1.add("آستانه مجاز پایین");
        sp1.add("آستانه مجاز بالا");
        sp1.add("تعداد سهام");
        sp1.add("ارزش بازار");
        sp1.add("قیمت خرید - سطر اول");
        sp1.add("تعداد خریدار - سطر اول");
        sp1.add("حجم خرید- سطر اول");
        sp1.add("قیمت فروش - سطر اول");
        sp1.add("تعداد فروشنده - سطر اول");
        sp1.add("حجم فروش- سطر اول");
        sp1.add("قیمت خرید - سطر دوم");
        sp1.add("تعداد خریدار - سطر دوم");
        sp1.add("حجم خرید- سطر دوم");
        sp1.add("قیمت فروش - سطر دوم");
        sp1.add("تعداد فروشنده - سطر دوم");
        sp1.add("حجم فروش- سطر دوم");
        sp1.add("قیمت خرید - سطر سوم");
        sp1.add("تعداد خریدار - سطر سوم");
        sp1.add("حجم خرید- سطر سوم");
        sp1.add("قیمت فروش - سطر سوم");
        sp1.add("تعداد فروشنده - سطر سوم");
        sp1.add("حجم فروش- سطر سوم");
        sp1.add("حجم مبنا");
        sp1.add("تعداد خریدار حقیقی");
        sp1.add("تعداد خریدار حقوقی");
        sp1.add("حجم خرید حقیقی");
        sp1.add("حجم خرید حقوقی");
        sp1.add("تعداد فروشنده حقیقی");
        sp1.add("تعداد فروشنده حقوقی");
        sp1.add("حجم فروش حقیقی");
        sp1.add("حجم فروش حقوقی");

        sp2 = new ArrayList<>();
        sp2.add("برابر");
        sp2.add("بزرگتر مساوی");
        sp2.add("کوچکتر مساوی");
        sp2.add("بزرگتر");
        sp2.add("کوچکتر");
    }

    private void loadSaved(String name, String cond) {
        utils=new ArrayList<>();
        ed.setText(name);
        Log.e("ss",cond);
        if(cond.contains("&&")){
            String[] c = cond.split("&&");
            for(int i =0; i<c.length; i++){
                utils.add(parse(c[i]));
            }
        }else{
            utils.add(parse(cond));
        }

        

        
    }
   AddSegments parse(String t){
        String one, two, split = "";
        int a,b;
        if(t.contains("==")){
           one =  t.split("==")[0];
           split = "==";
           findSP1(one);
           findSP2(split);
           two =  t.split("==")[1];
           return new AddSegments(  findSP1(one),  findSP2(split),(two));
        }
        else if(t.contains("<=")){
            one =  t.split("<=")[0];
            split = "<=";
            findSP1(one);
            findSP2(split);
            two =  t.split("<=")[1];
            return new AddSegments(  findSP1(one),  findSP2(split),(two));
        }else if(t.contains("!=")){
            one =  t.split("!=")[0];
            split = "!=";
            findSP1(one);
            findSP2(split);
            two =  t.split("!=")[1];
            return new AddSegments(  findSP1(one),  findSP2(split),(two));
        }
        else if(t.contains(">=")){
            one =  t.split(">=")[0];
            split = ">=";
            findSP1(one);
            findSP2(split);
            two =  t.split(">=")[1];
            return new AddSegments(  findSP1(one),  findSP2(split),(two));
        }
        else if(t.contains(">")){
            one =  t.split(">")[0];
            split = ">";
            findSP1(one);
            findSP2(split);
            two =  t.split(">")[1];
            return new AddSegments(  findSP1(one),  findSP2(split),(two));
        }else if(t.contains("<")){
            one =  t.split("<")[0];
            split = "<";
            findSP1(one);
            findSP2(split);
            two =  t.split("<")[1];
            return new AddSegments(  findSP1(one),  findSP2(split),(two));
        }
        return null;
        
    }
    int findSP1(String o){
        String[] f = new FilterFieldHelper().conditions;
       
        for(int i =0; i<f.length; i++){
            if(f[i].equals(o)) {
                return i;
            }
        }

        return  0;

    }
    int findSP2(String o){
        String[] f = new FilterFieldHelper().function;
        for(int i =0; i<f.length; i++){
            if(f[i].equals(o)) {
                return i;
            }
        }

        return  0;

    }


    int p = 0;
    private void setTileActionbar() {


        final TextView tit = findViewById(R.id.app_name);

        tit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(p++%3 == 0){
                    animation(tit, 10000, Techniques.Tada,false);
                }

            }
        });
        ImageView back = findViewById(R.id.imageView3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tit.setText("فیلتر نویسی ساده");
    }
    private void initiateDB() {
        db = new Helper(this);
    }

    private void declareButton() {
        ed = findViewById(R.id.ed);
        submit = findViewById(R.id.btn_submit);
        addFilter = findViewById(R.id.btn_add_filter);
        addFilter.setCustomTextFont(R.font.iranyekanmedium);
        submit.setCustomTextFont(R.font.iranyekanmedium);
        addFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFilterToRecycler();
            }
        });

    }
    public String SaveData(){
        FilterFieldHelper helper = new FilterFieldHelper();
        String out = "";
        Float f = 0f;


        for(int i = 0; i < utils.size(); i++){
            try {
                f = Float.parseFloat(utils.get(i).getVal());
            }catch (NumberFormatException | IllegalStateException r){
                new ToastMaker(getBaseContext(),"خطا در تجزیه فیلتر ورودی ها را کنترل کنید");
                return "";
            }

            out += helper.conditions[utils.get(i).getSp1()] + helper.function[utils.get(i).getSp2()] + utils.get(i).getVal() + ((i == utils.size()-1) ? "":"&&");
        }
       return out;
    }
    public void addFilterToRecycler(){


            utils.add(new AddSegments(0,0,"0"));
            adapter.notifyItemInserted(utils.size()-1);



    }
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) (SimpleAdder.this).getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = (SimpleAdder.this).getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View((SimpleAdder.this));
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void removeFilterToRecycler(AddSegments item){
        int index = utils.indexOf(item);
        utils.remove(index);

        adapter.notifyItemRangeRemoved(index,1);


    }
    String name, condition;
    public void Submit(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name =ed.getText().toString();
                if(!name.equals("")){

                    if(db.checkExists(name) == (EDITMODE == 1)) {

                        try {

                            loading.setVisibility(View.VISIBLE);
                            submit.setText("");
                            AsyncHttpClient client = new AsyncHttpClient();
                            JSONObject obj = new JSONObject();

                            String data = SaveData();
                            obj.put("token", "fpa");
                            if(!data.equals("")) {


                                obj.put("condition", data);

                                StringEntity entity = new StringEntity(obj.toString());

                                client.post(getApplicationContext(), "http://sourcearena.ir/api/filter.php", entity, "application/json", new TextHttpResponseHandler() {

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                        loading.setVisibility(View.GONE);
                                        submit.setText("ثبت");
                                        Log.d("LoginActivity", "body " + responseString);
                                    }

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                        loading.setVisibility(View.GONE);
                                        submit.setText("ثبت");
                                        try {
                                            JSONObject respObj = new JSONObject(responseString);
                                            int size = respObj.getInt("count");
                                            String condition = respObj.getString("calculate");
                                            hideKeyboard();
                                            showCount(size);
                                            SimpleAdder.this.name = name;
                                            SimpleAdder.this.condition = condition;
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }else{
                                loading.setVisibility(View.GONE);
                                submit.setText("ثبت");
                            }
                        } catch (Exception ex) {
                            new ToastMaker(getApplicationContext(),"خطا در تجزیه فیلتر ورودی ها را کنترل کنید");
                        }

                    }else{
                        new ToastMaker(getApplicationContext(),"عنوان فیلتر تکراری است");
                    }

                }else{
                    animation(ed,0,Techniques.Shake,false);
                    ed.requestFocus();
                }

            }
        });

    } int i = 0;
    public void animation(final View view, final int time, final Techniques t, final boolean repeat) {
        final Handler handler = new Handler();
        if(repeat) {
            final Runnable r = new Runnable() {
                public void run() {
                    YoYo.with(t)
                            .duration(700)
                            .playOn(view);

                    handler.postDelayed(this, time);


                }
            };


            handler.postDelayed(r, time);
        }else{
            YoYo.with(t)
                    .duration(500)
                    .playOn(view);
        }



    }

    void showCount(int size){


    DialogPlus dialog = DialogPlus.newDialog(this).setContentHolder(new dialogAdapter(R.layout.dialog_filter_add, size+"",getLayoutInflater()))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {


                        if (view.getId() == R.id.btn_cancel) {
                            dialog.dismiss();
                        }
                        else if (view.getId() == R.id.btn_add_filter) {

                            if(EDITMODE == 1){
                                db.editFilter(name,condition);
                            }else{
                                db.addFilter(new Filter(name, condition,1));
                            }

                            Intent intent = getIntent();
                            intent.putExtra("added", true);
                            setResult(RESULT_OK, intent);
                            finish();



                        }
                    }
                }).setGravity(Gravity.CENTER)
                .setExpanded(false)

                .create();



        TextView tv = dialog.getHolderView().findViewById(R.id.textView17);
        tv.setText(size+"");
        dialog.show();




    }
    private void declareRecycler() {

        rv = findViewById(R.id.filter_recycle);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(layoutManager);

        if(EDITMODE == 0){
            utils.add(new AddSegments(0,0,"0"));

        }




        adapter = new RecyclerAdapter(getBaseContext(), utils, new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AddSegments item) {



                removeFilterToRecycler(item);




            }
        });

        rv.post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemRangeChanged(0,utils.size());
            }
        });

        rv.setAdapter(adapter);

    }


}
