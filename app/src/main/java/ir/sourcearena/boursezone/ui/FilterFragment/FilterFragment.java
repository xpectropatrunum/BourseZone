package ir.sourcearena.boursezone.ui.FilterFragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.baoyz.widget.PullRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ir.sourcearena.boursezone.tools.GetUser;
import ir.sourcearena.boursezone.Account.Purchase.Purchase;
import cz.msebera.android.httpclient.Header;
import ir.sourcearena.boursezone.FilterShowActivity;
import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.ui.dialog.adapter;
import mehdi.sakout.fancybuttons.FancyButton;

public class FilterFragment extends Fragment {



    GridView simpleGrid;
    CustomAdapter customAdapter;
    PullRefreshLayout ref;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.a_page_appfilter, container, false);

        connect(Settings.ALL_FILTERS);


        simpleGrid = (GridView) root.findViewById(R.id.simpleGridView);


        simpleGrid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        ref = (PullRefreshLayout) root.findViewById(R.id.refresher);
        ref.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        ref.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                connect(Settings.ALL_FILTERS);
            }
        });
        return root;


    }
    void newThread(final GridView cv, final CustomAdapter c) {
        new Thread(new Runnable() {


            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cv.setAdapter(c);
                    }
                });
            }

        }).start();
    }
    public static FilterFragment newInstance(String text) {

        FilterFragment f = new FilterFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }



      /*new String[]{"&top_in", "بیشترین مقدار ورود پول"},
            new String[]{"&top_out", "بیشترین مقدار خروج پول"},
            new String[]{"&real_buy_per", "بیشترین سرانه خرید حقیقی"},
            new String[]{"&real_sell_per", "بیشترین سرانه فروش حقیقی"},
            new String[]{"&co_buy_per", "بیشترین سرانه خرید حقوقی"},
            new String[]{"&co_sell_per", "بیشترین سرانه فروش حقوقی"},
            new String[]{"&sell_q_val", "بیشترین ارزش صف فروش"},
            new String[]{"&sell_q_vol", "بیشترین حجم صف فروش"},
            new String[]{"&buy_q_val", "بیشترین ارزش صف خرید"},
            new String[]{"&buy_q_vol", "بیشترین حجم صف خرید"},*/


        List<String> titless, btns, methods;

        public void ParseJSon(String data) throws JSONException {

            titless = new ArrayList<>();
            btns = new ArrayList<>();
            methods = new ArrayList<>();
            JSONArray arr = new JSONArray(data);
            for (int i = 0; i < arr.length(); i++) {
                titless.add(arr.getJSONArray(i).getString(1));
                methods.add(arr.getJSONArray(i).getString(0));
                btns.add(arr.getJSONArray(i).getString(2));

            }
            customAdapter = new CustomAdapter(getContext(), btns);
            newThread(simpleGrid,customAdapter);

            simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String appendix = methods.get(i);
                    String title = titless.get(i);
                    if(i < 14){
                        Intent in = new Intent(getContext(), FilterShowActivity.class);
                        in.putExtra("appendix", appendix);
                        in.putExtra("title", title);
                        startActivity(in);
                    }else {
                        if (new GetUser(getContext()).isPremium()) {
                            Intent in = new Intent(getContext(), FilterShowActivity.class);
                            in.putExtra("appendix", appendix);
                            in.putExtra("title", title);
                            startActivity(in);
                        } else {

                                            final DialogPlus dialog = DialogPlus.newDialog(getActivity())
                                                    .setContentHolder(new adapter(R.layout.dialog_is_not_premium, "", getActivity()))
                                                    .setOnClickListener(new OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogPlus dialog, View view) {
                                                            if(view.getId() == R.id.btn_cancel)
                                                            {
                                                                dialog.dismiss();
                                                            }

                                                        }
                                                    })
                                                    .setGravity(Gravity.CENTER)
                                                    .setExpanded(false)
                                                    .create();
                                            dialog.show();
                                            TextView tv = dialog.getHolderView().findViewById(R.id.textView19);
                                            tv.setText("برای مشاهده فیلتر های پیشرفته اکانت خود را ارتقا دهید");
                            FancyButton buy = dialog.getHolderView().findViewById(R.id.btn_add_filter);
                            buy.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent in = new Intent(getContext(), Purchase.class);

                                    startActivity(in);
                                }
                            });


                        }
                    }

                }
            });
            ref.setRefreshing(false);

        }
    public void connect(final String url) {
        try {

            AsyncHttpClient client = new AsyncHttpClient();


            client.post(getContext(), url, null, "application/json;  text/html; charset=utf-8;", new TextHttpResponseHandler() {

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("LoginActivity", "Failed");
                    Log.d("LoginActivity", "body " + responseString);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {

                    try {
                        ParseJSon(responseString);


                    } catch (JSONException | NullPointerException d) {
                        d.printStackTrace();
                    }
                }
            });
        } catch (Exception ex) {
            Log.d("LoginActivity", "Getting Exception " + ex.toString());
        }


    }


}