package ir.sourcearena.boursezone.Account.Purchase;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.tools.GetUser;
import ir.sourcearena.boursezone.R;

public class Purchase extends AppCompatActivity {
    GridView simpleGrid;
    GetUser gu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);
        ActionBar_();
        gu = new GetUser(this);

        connect(Settings.CHECK_TIME+gu.getNumber(),0);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView);


        simpleGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));

       connect(Settings.PRICES,1);

    }
    private void ActionBar_() {

        TextView title = findViewById(R.id.app_name);
        ImageView back = findViewById(R.id.imageView3);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }
    void newThread(final GridView cv, final CustomAdapter c) {
        new Thread(new Runnable() {


            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cv.setAdapter(c);
                    }
                });
            }

        }).start();
    }
    List<String[]> a;
    public void ParseJSon(String data) throws JSONException {


        a = new ArrayList<>();


       JSONArray arr = new JSONArray(data);
        for (int i = 0; i < arr.length(); i++) {
            a.add(new String[]{arr.getJSONArray(i).getString(0),
                    arr.getJSONArray(i).getString(1),
                    arr.getJSONArray(i).getString(2)});

        }
        CustomAdapter customAdapter = new CustomAdapter(this, a);
        newThread(simpleGrid,customAdapter);

        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                byte[] data = new byte[0];
                try {
                    data = ( a.get(i)[2]+","+gu.getNumber()+","+a.get(i)[0] ).getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                Uri uri = Uri.parse(Settings.ZARIN_GATE+"?cc="+base64);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);


            }
        });


    }
    public void connect(final String url, final int a) {
        try {

            AsyncHttpClient client = new AsyncHttpClient();


            client.post(this, url, null, "application/json;  text/html; charset=utf-8;", new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("LoginActivity", "Failed");
                    Log.d("LoginActivity", "body " + responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    if(a==1){
                        try {
                            ParseJSon(responseString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{


                            validate(responseString);

                    }




                }
            });
        } catch (Exception ex) {
            Log.d("LoginActivity", "Getting Exception " + ex.toString());
        }


    }

    private void validate(String responseString) {
        if(!responseString.equals("")) {
            String[] a = responseString.split(",");
            gu.putPremium(Float.valueOf(a[1]) > 0);
            gu.putTime(Float.valueOf(a[1]));
            TextView tv = findViewById(R.id.textView43);
            if (gu.getTime() > 0) {
                tv.setText("از اشتراک شما " + gu.getTime() + " روز باقی مانده است");
            }
        }
    }

}
