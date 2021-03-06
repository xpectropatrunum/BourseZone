package ir.sourcearena.boursezone.stockholder;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;


import com.synnapps.carouselview.ViewListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import de.codecrafters.tableview.TableHeaderAdapter;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.ui.LoadingView;
import ir.sourcearena.boursezone.R;

public class stockholder extends Fragment {
    View root;



    AsyncTask task = null;
    String title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.stockholder, container, false);


        list = new ArrayList<>();
        title = getActivity().getIntent().getExtras().getString("title", "");
        setChart();
        task = new Request().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Settings.JSON_STOCKHOLDER + title);
        initializeTableView();
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (task != null) {
            task.cancel(true);

        }
    }

    private void initializeTableView() {

        TableView<StockHolderUtil> tableView = (TableView<StockHolderUtil>) root.findViewById(R.id.tableView);
        HeaderTableDataAdapter he = new HeaderTableDataAdapter(getActivity());
        tableView.setHeaderAdapter(he);

        tableView.setHeaderBackground(R.drawable.header_bg);
        CarTableDataAdapter ca = new CarTableDataAdapter(getActivity(), list);
        tableView.setDataAdapter(ca);

        TableColumnWeightModel columnModel = new TableColumnWeightModel(4);
        columnModel.setColumnWeight(0, 2);
        columnModel.setColumnWeight(1, 1);
        columnModel.setColumnWeight(2, 1);
        columnModel.setColumnWeight(3, 1);
        tableView.setHeaderElevation(3);
        tableView.setColumnModel(columnModel);


        //Log.e("s",size/4+"");
    }

    List<StockHolderUtil> list;

    public class HeaderTableDataAdapter extends TableHeaderAdapter {
        public HeaderTableDataAdapter(Context context) {
            super(context);
        }

        @Override
        public View getHeaderView(int columnIndex, ViewGroup parentView) {

            return render(columnIndex);
        }

        private View render(int pos) {
            String tx;
            switch (pos) {
                case 0:
                    tx = "سهامدار";
                    break;

                case 1:
                    tx = "حجم";
                    break;

                case 2:
                    tx = "درصد";
                    break;

                case 3:
                    tx = "تغییر";


                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + pos);
            }


            final TextView textView = new TextView(getContext());
            textView.setText(tx);
            textView.setPadding(20, 10, 20, 10);
            textView.setTypeface(ResourcesCompat.getFont(getContext(),R.font.iransansbold));
            textView.setTextSize(12);

            textView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.pureWhite,null));            if(pos != 0){
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
            }


            return textView;
        }


    }

    ;

    public class CarTableDataAdapter extends TableDataAdapter<StockHolderUtil> {

        public CarTableDataAdapter(Context context, List<StockHolderUtil> data) {
            super(context, data);
        }

        private View render(final StockHolderUtil car, int pos) {
            String tx;
            switch (pos) {
                case 0:
                    tx = car.getName();

                    break;

                case 1:
                    tx = car.getFname();
                    break;

                case 2:
                    tx = car.getMarket();
                    break;

                case 3:
                    tx = car.getRT();


                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + pos);
            }


            final TextView textView = new TextView(getContext());
            textView.setText(tx);
            textView.setTextColor(ResourcesCompat.getColor(getResources(),R.color.text1,null));
            textView.setTypeface(ResourcesCompat.getFont(getContext(),R.font.iransanslight));
            textView.setPadding(20, 10, 20, 10);
            textView.setTextSize(12);
            if(pos != 0){
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                textView.setTextDirection(View.TEXT_DIRECTION_LTR);
            }else{    textView.setTextDirection(View.TEXT_DIRECTION_RTL);
                textView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                textView.setGravity(Gravity.RIGHT);
            }

            return textView;
        }

        @Override
        public View getCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
            StockHolderUtil car = getRowData(rowIndex);
            View renderedView = render(car, columnIndex);
            size += renderedView.getHeight();


            return renderedView;
        }

    }

    int size = 0;
    ViewListener viewListener = new ViewListener() {

        @Override
        public View setViewForPosition(int position) {
            View customView = getLayoutInflater().inflate(R.layout.carousel_view, null);
            TextView tv1 = customView.findViewById(R.id.market_name);
            TextView tv2 = customView.findViewById(R.id.index_caro);
            TextView tv3 = customView.findViewById(R.id.index_change_caro);
            TextView tv4 = customView.findViewById(R.id.change_price_caro);
            tv1.setText(" سهام داران");
            tv2.setVisibility(View.GONE);
            tv3.setVisibility(View.GONE);
            tv4.setVisibility(View.GONE);


            return customView;
        }
    };


    public void setChart() {


        WebView wv = root.findViewById(R.id.webchart);
        wv.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.lightcard,null));
        WebSettings settings = wv.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        settings.setJavaScriptEnabled(true);
        wv.loadUrl("http://sourcearena.ir/charts/sholder.php?bg="+getResources().getString(R.color.lightcard).replace("#","")+"&name="+title);


    }

    public void ParseJSon(String res) throws JSONException {


        JSONArray ja = new JSONArray(res);
        for (int i = 0; i < ja.length(); i++) {
            list.add(new StockHolderUtil(
                    ja.getJSONObject(i).getString("name"),
                    ja.getJSONObject(i).getString("volume2"),
                    ja.getJSONObject(i).getString("percent"), ja.getJSONObject(i).getString("change")));


        }
        initializeTableView();

    }

    private class Request extends AsyncTask<String, Void, String> {

        StringBuffer stringBuffer;

        @Override
        protected String doInBackground(String... params) {

            try {


                URL url = new URL(params[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
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
            return stringBuffer.toString();


        }


        @Override
        protected void onPostExecute(String result) {

            try {


                ParseJSon(result);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
