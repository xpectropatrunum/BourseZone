package ir.sourcearena.filterbourse.filteryab.simple;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.dbhelper.Filter;
import ir.sourcearena.filterbourse.dbhelper.Helper;
import ir.sourcearena.filterbourse.filteryab.simple.adapter.AddSegments;
import ir.sourcearena.filterbourse.filteryab.simple.adapter.FilterFieldHelper;
import ir.sourcearena.filterbourse.filteryab.simple.adapter.RecyclerAdapter;
import ir.sourcearena.filterbourse.filteryab.simple.adapter.dialogAdapter;
import mehdi.sakout.fancybuttons.FancyButton;

public class SimpleAdder extends AppCompatActivity {
    RecyclerAdapter adapter;
    FancyButton addFilter, submit;
    RecyclerView rv;
    EditText ed;
    Helper db;
    List<AddSegments> utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsimplefilter);

        declareRecycler();
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

        tit.setText("فیلتر ساده");
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

        for(int i = 0; i < utils.size(); i++){
            out += helper.conditions[utils.get(i).getSp1()] + helper.function[utils.get(i).getSp2()] + utils.get(i).getVal() + ((i == utils.size()-1) ? "":"&&");
        }
       return out;
    }
    public void addFilterToRecycler(){

        utils.add(new AddSegments(0,0,0));
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

                    if(!db.checkExists(name)) {

                        try {

                            AsyncHttpClient client = new AsyncHttpClient();
                            JSONObject obj = new JSONObject();

                            obj.put("token", "fpa");
                            obj.put("condition", SaveData());

                            StringEntity entity = new StringEntity(obj.toString());

                            client.post(getApplicationContext(), "http://sourcearena.ir/api/filter.php", entity, "application/json", new TextHttpResponseHandler() {

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                    Log.d("LoginActivity", "Failed");
                                    Log.d("LoginActivity", "body " + responseString);
                                }

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, String responseString) {

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
                        } catch (Exception ex) {
                            Log.d("LoginActivity", "Getting Exception " + ex.toString());
                        }

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

                            db.addFilter(new Filter(name, condition));
                            Intent intent = getIntent();
                            intent.putExtra("added", true);
                            setResult(RESULT_OK, intent);
                            finish();



                        }
                    }
                }).setGravity(Gravity.CENTER)
                .setExpanded(true)  // This will enable the expand feature, (similar to android L share dialog)
                .create();


        TextView tv = dialog.getHolderView().findViewById(R.id.textView17);
        tv.setText(size+"");
        dialog.show();




    }
    private void declareRecycler() {

        rv = findViewById(R.id.filter_recycle);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        rv.setLayoutManager(layoutManager);

        utils = new ArrayList<>();
        utils.add(new AddSegments(0,0,0));




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
