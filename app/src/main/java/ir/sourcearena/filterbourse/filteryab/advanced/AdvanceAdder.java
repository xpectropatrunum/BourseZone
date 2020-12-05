package ir.sourcearena.filterbourse.filteryab.advanced;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;
import ir.sourcearena.filterbourse.dbhelper.Filter;
import ir.sourcearena.filterbourse.dbhelper.Helper;
import ir.sourcearena.filterbourse.filteryab.simple.adapter.dialogAdapter;
import ir.sourcearena.filterbourse.tools.ToastMaker;
import mehdi.sakout.fancybuttons.FancyButton;

public class AdvanceAdder extends AppCompatActivity {

    FancyButton addFilter, submit;

    EditText ed,ed2;
    Helper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addadvancedfilter);


        declareButton();
        Submit();
        initiateDB();
        setTileActionbar();




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

        tit.setText("فیلترنویسی حرفه ای");
    }
    private void initiateDB() {
        db = new Helper(this);
    }

    private void declareButton() {
        ed = findViewById(R.id.ed);
        ed2 = findViewById(R.id.ed2);
        submit = findViewById(R.id.btn_submit);
        addFilter = findViewById(R.id.btn_show);
        addFilter.setCustomTextFont(R.font.iranyekanmedium);
        submit.setCustomTextFont(R.font.iranyekanmedium);
        addFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showField();
            }
        });


    }


    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) (AdvanceAdder.this).getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = (AdvanceAdder.this).getCurrentFocus();
        if (view == null) {
            view = new View((AdvanceAdder.this));
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    String name, condition;
    public void Submit(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name =ed.getText().toString();
                final String filter =ed2.getText().toString();
                if(!name.equals("")){

                    if(!db.checkExists(name)) {
                        if(!filter.equals("")){
                        try {

                            AsyncHttpClient client = new AsyncHttpClient();
                            JSONObject obj = new JSONObject();

                            obj.put("token", "fpa");
                            obj.put("condition", filter);

                            StringEntity entity = new StringEntity(obj.toString());

                            client.post(getApplicationContext(), "http://sourcearena.ir/api/filter.php", entity, "application/json", new TextHttpResponseHandler() {

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                                    Log.d("LoginActivity", "body " + responseString);
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                                    try {
                                        JSONObject respObj = new JSONObject(responseString);
                                        int size = respObj.getInt("count");
                                        hideKeyboard();
                                        showCount(size);
                                        AdvanceAdder.this.name = name;
                                        AdvanceAdder.this.condition = filter;
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception ex) {
                            Log.d("LoginActivity", "Getting Exception " + ex.toString());
                        }
                        }else{
                            animation(ed2,0,Techniques.Shake,false);
                            ed2.requestFocus();
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
    void showField(){
        DialogPlus dialog = DialogPlus.newDialog(this).setContentHolder(new dialogAdapter(R.layout.dialog_fields,"",getLayoutInflater()))
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {


                        if (view.getId() == R.id.btn_cancel) {
                            dialog.dismiss();
                        }
                        else if (view.getId() == R.id.btn_add_filter) {

                            db.addFilter(new Filter(name, condition,2));
                            Intent intent = getIntent();
                            intent.putExtra("added", true);
                            setResult(RESULT_OK, intent);
                            finish();



                        }
                    }
                }).setGravity(Gravity.CENTER)
                .setExpanded(true)
                .create();
        WebView web = dialog.getHolderView().findViewById(R.id.web_field);
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl(Settings.ADVANCED_FIELD);
        dialog.show();
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

                            db.addFilter(new Filter(name, condition,1));
                            Intent intent = getIntent();
                            intent.putExtra("added", true);
                            setResult(RESULT_OK, intent);
                            finish();



                        }
                    }
                }).setGravity(Gravity.CENTER)
                .setExpanded(true)
                .create();


        TextView tv = dialog.getHolderView().findViewById(R.id.textView17);
        tv.setText(size+"");
        dialog.show();




    }





}
