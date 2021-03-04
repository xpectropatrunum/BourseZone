package ir.sourcearena.boursezone.Account;

import ir.sourcearena.boursezone.Account.Purchase.Purchase;
import ir.sourcearena.boursezone.MainActivity;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.tools.GetUser;
import ir.sourcearena.boursezone.tools.ToastMaker;
import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.ui.dialog.adapter;
import mehdi.sakout.fancybuttons.FancyButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Login extends AppCompatActivity {

    FancyButton submit, later;
    EditText ed,ed2;
    TextView tv, label, policy;
    GetUser gu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        STAGE = 100;
        new Request().execute("https://sourcearena.ir/androidFilterApi/app/login/memberlist.php");

        gu = new GetUser(getBaseContext());
        defineViews();
        actionViews();
    }

    private void actionViews() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ed.getText().toString().equals("")){
                    new Request().execute(Settings.LOGIN_API+"?phone="+ed.getText().toString());
                }else{
                    YoYo.with(Techniques.Shake).duration(1000).playOn(ed);
                }

            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gu.putFirst();

                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ));

            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final DialogPlus dialog = DialogPlus.newDialog(Login.this)
                        .setContentHolder(new adapter(R.layout.dialog_is_not_premium, "", Login.this))
                        .setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(DialogPlus dialog, View view) {
                                if (view.getId() == R.id.btn_cancel) {
                                    dialog.dismiss();
                                }


                            }
                        })
                        .setGravity(Gravity.CENTER)
                        .setExpanded(false)
                        .create();
                dialog.show();
                TextView tv = dialog.getHolderView().findViewById(R.id.textView19);
                tv.setText(R.string.policy);
                TextView tt = dialog.getHolderView().findViewById(R.id.textView24);
                tt.setText("توجه");
                FancyButton dis = dialog.getHolderView().findViewById(R.id.btn_cancel);
                FancyButton buy = dialog.getHolderView().findViewById(R.id.btn_add_filter);
                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
dialog.dismiss();
                    }
                });
                buy.setText("متوجه شدم");
                dis.setVisibility(View.GONE);

            }
        });
    }

    private void defineViews() {
        submit = findViewById(R.id.btn_login);
        later = findViewById(R.id.btn_later);
        ed = findViewById(R.id.phone_number);
        ed2 = findViewById(R.id.username);
        tv = findViewById(R.id.textView30);
        policy = findViewById(R.id.textView45);
        label = findViewById(R.id.textView4);
    }

    private class Request extends AsyncTask<String, Void, String> {

        StringBuffer stringBuffer;

        @Override
        protected String doInBackground(String... params) {
            stringBuffer = null;
            try {





                URL url = new URL(params[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Connection", "close");

                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();



                int code = httpURLConnection.getResponseCode();
                if (code == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuffer = new StringBuffer();
                    String line;

                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }


                }

            } catch (Exception e) {

                e.printStackTrace();
            }
            if (stringBuffer == null) {
                return "";
            }
            return stringBuffer.toString();


        }


        int p = 0;

        @Override
        protected void onPostExecute(final String result) {
            if(STAGE == 2){
                finished(result);
            }if(STAGE == 100){
                list = result.split(",,");
                STAGE = 0;
            }else{
                nextLevel(result);
            }




        }
    }
    String[] list;

    private void finished(String res) {
        if(res.equals("OK")){
            new ToastMaker(getBaseContext(),"خوش آمدید");

            gu.putUsername(username);
            gu.putName(name);

            gu.putPremium(false);
            gu.putNumber(phone);
            gu.putTime(0f);
            gu.putFirst();
            startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ));
        }else{
            new ToastMaker(getBaseContext(),"خطا" +
                    "");
        }

    }

    String phone;
    private void nextLevel(String result) {

        final String[] m = result.split(",");

        try {
            int k = Integer.parseInt(m[0]);
            if(k > 0){
                ed.setText("");
                label.setText("کد 6 رقمی را وارد کنید");
                tv.setVisibility(View.VISIBLE);
                tv.setText("کد به "+m[2]+" ارسال شد");
                later.setEnabled(false);
                ed.setHint("123456");
                phone = m[2];


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!ed.getText().toString().equals("")){

                            if(ed.getText().toString().equals(m[1])){
                                if(m[3].equals("1")){
                                    GetUser gu = new GetUser(getBaseContext());
                                    gu.putUsername(m[4]);
                                    gu.putName(m[6]);
                                    gu.putNumber(phone);
                                    Float f = Float.parseFloat(m[5]);
                                    if(f > 0){
                                        gu.putPremium(true);
                                        gu.putTime(f);
                                    }else{
                                        gu.putPremium(false);
                                        gu.putTime(0f);
                                    }
                                    gu.putFirst();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP ));
                                    new ToastMaker(getBaseContext(),"وارد شدید");
                                }else{
                                    finishRegister();
                                }


                            }else{
                                YoYo.with(Techniques.Shake).duration(1000).playOn(ed);
                                new ToastMaker(getBaseContext(),"کد اشتباه است");
                            }
                        }else{
                            YoYo.with(Techniques.Shake).duration(1000).playOn(ed);
                        }
                    }
                });

            }


        }catch (NumberFormatException e){
            tv.setVisibility(View.VISIBLE);
            tv.setText("خطا در ارسال کد لطفا دوباره تلاش کنید");
        }


    }
    String username, name ;
    int STAGE = 0;
    private void finishRegister() {
        STAGE = 2;
        label.setText("نام کاربری");
        tv.setVisibility(View.INVISIBLE);
        ed.setHint("bourse_zone");
        ed2.setHint("بورس زون");
        ed.setInputType(InputType.TYPE_CLASS_TEXT);
        ed.setText("");
        LinearLayout ll = findViewById(R.id.linearLayout7);
        ll.setVisibility(View.VISIBLE);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int km = 0;
                username = ed.getText().toString();
                name = ed2.getText().toString();
                boolean valid = (username != null) && username.matches("^[a-zA-Z0-9]([._](?![._])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$");
                if(valid){
                    for (String name: list) {
                        if(name.equals(username)){
                            new ToastMaker(getBaseContext(),"این نام کاربری موجود است");
                            km = 1;
                        }

                    }
                    if(km == 0){

                        new Request().execute(Settings.REGISTER_API+"?phone="+phone+"&username="+username+"&name="+name);
                    }

                }else{
                    YoYo.with(Techniques.Shake).duration(1000).playOn(ed);
                    new ToastMaker(getBaseContext(),"نام کاربری معتبر نیست");
                }
            }
        });

    }
}
