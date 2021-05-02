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
import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
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
import ir.sourcearena.boursezone.tools.ToastMaker;

public class Purchase extends AppCompatActivity {
    GridView simpleGrid;
    GetUser gu;

    static final String TAG = "sourcearena";

    // SKUs for our products: the premium upgrade (non-consumable)
    static String SKU_PREMIUM = "";

    // Does the user have the premium upgrade?
    boolean mIsPremium = false;

    // (arbitrary) request code for the purchase flow
    static int RC_REQUEST = 100;

    int desc = -1;
    // The helper object
    IabHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity);
        ActionBar_();
        gu = new GetUser(this);


        String base64EncodedPublicKey = "MIHNMA0GCSqGSIb3DQEBAQUAA4G7ADCBtwKBrwDCDVQNRxBx9W2bbsGpE5U3+t//yRz9ZWl+TYbLb8MtFofJ5ZjYAy88XL+MCzC+w1fhxH5mTiWo7yDAxPwInAbGEJb61RI0akj3ogrLbOZxfSWMN7B10WzEOjAVNkDBAve1jITrZJvVkF20/3nBUSiKBWOLXmoays6D5DmliniXzUH2+HAGn6CLenUdFJ5qc6maU6aHBIhkbjz7HWFpq+szRULdmjyuUnuK4X0JDfkCAwEAAQ==";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {


                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.

                }
                // Hooray, IAB is fully set up!

            }
        });


        connect(Settings.CHECK_TIME+gu.getNumber(),0);
        simpleGrid = (GridView) findViewById(R.id.simpleGridView);
        simpleGrid.setVisibility(View.GONE);

        simpleGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));

        if(gu.getTime() <=  0){
            simpleGrid.setVisibility(View.VISIBLE);
            connect(Settings.PRICES,1);
        }


    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            if (result.isFailure()) {

                return;
            }
            else {

                // does the user have the premium upgrade?
                mIsPremium = inventory.hasPurchase(SKU_PREMIUM);


                connect("http://sourcearena.ir/androidFilterApi/app/purchase/zarinpalverify.php?OK&phone="+gu.getNumber()+"&desc="+desc,100);


            }


        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            if (resultCode == RESULT_OK) {
                connect("http://sourcearena.ir/androidFilterApi/app/purchase/zarinpalverify.php?OK&phone=" + gu.getNumber() + "&desc=" + requestCode, 100);
                new ToastMaker(getApplication(), "اکانت شما با موفقیت ارتقا یافت");
                simpleGrid.setVisibility(View.GONE);
            }else{
                new ToastMaker(getApplication(), "خطا در ارتقای اکانت");
            }
        }
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


                SKU_PREMIUM = "c"+((i+1)>3?5:i+1);
                RC_REQUEST = i;
               try{

                    mHelper.launchPurchaseFlow(Purchase.this, SKU_PREMIUM, RC_REQUEST, null, "payload-string");
                }catch ( java.lang.IllegalStateException c){
                    new ToastMaker(Purchase.this, "برنامه بازار را نصب کنید");

                }



              /* byte[] data = new byte[0];
                try {
                    data = ( a.get(i)[2]+","+gu.getNumber()+","+a.get(i)[0] ).getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                Uri uri = Uri.parse(Settings.ZARIN_GATE+"?cc="+base64);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);

               */


            }
        });


    }
    public void connect(final String url, final int a) {
        try {

            AsyncHttpClient client = new AsyncHttpClient();


            client.post(this, url, null, "application/json;  text/html; charset=utf-8;", new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {


                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    if(a==100){
                        connect(Settings.CHECK_TIME+gu.getNumber(),0);
                    }
                    else if(a==1){
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
