package ir.sourcearena.filterbourse;

import android.content.Context;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.sip.SipSession;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<FilterUtils> utils;
    Settings setting;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FilterUtils item);
    }
    public RecyclerAdapter(Context context, List utils, OnItemClickListener listener) {
        this.context = context;
        this.utils = utils;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_filter_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    FilterUtils pu;
    boolean reverse;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setting = new Settings();
        holder.itemView.setTag(utils.get(position));

        holder.setIsRecyclable(false);
         pu = utils.get(position);


        holder.full_name.setText(pu.getName());

        holder.f1.setText(pu.getF1());
        holder.f2.setText(pu.getF2());
        holder.f3.setText(pu.getF3());
        holder.fn1.setText(pu.getFn1());
        holder.fn2.setText(pu.getFn2());
        holder.fn3.setText(pu.getFn3());
        if(pu.getFn2().equals("قدرت فروشنده")){
            reverse = true;
        }


        checkPercent(holder.f2);
        checkPercent(holder.f1);


        checkPercent(holder.f3);


        holder.bind(pu, listener);

    }
    private void checkPercent(TextView tv)  {
        String text = tv.getText().toString();
        text = PersianToEnglish(text);

        if(text.contains("%")){
            Float percent = Float.parseFloat(text.replace("%",""));

            if(percent > 0){

                tv.setTextColor(context.getResources().getColor(R.color.green));
                if(reverse ) {
                    tv.setTextColor(context.getResources().getColor(R.color.red));
                }
            }else{
                    tv.setTextColor(context.getResources().getColor(R.color.red));
                if(reverse ) {
                    tv.setTextColor(context.getResources().getColor(R.color.green));
                }
            }


            reverse = false;
        }

    }
    public static String PersianToEnglish(String persianStr)  {
        String answer = persianStr;
        answer = answer.replace("۱","1");
        answer = answer.replace("۲","2");
        answer = answer.replace("۳","3");
        answer = answer.replace("۴","4");
        answer = answer.replace("۵","5");
        answer = answer.replace("۶","6");
        answer = answer.replace("۷","7");
        answer = answer.replace("۸","8");
        answer = answer.replace("۹","9");
        answer = answer.replace("۰","0");
        return answer;
    }
    @Override
    public int getItemCount() {
        return utils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView full_name;

        public TextView f1;
        public TextView f2;
        public TextView f3;
        public TextView fn1;
        public TextView fn2;
        public TextView fn3;

        public ViewHolder(View itemView) {
            super(itemView);

            full_name = (TextView) itemView.findViewById(R.id.namad_full_name);

            f1 = (TextView) itemView.findViewById(R.id.custom_field_1);
            f2 = (TextView) itemView.findViewById(R.id.custom_field_2);
            f3 = (TextView) itemView.findViewById(R.id.custom_field_3);
            fn1 = (TextView) itemView.findViewById(R.id.custom_field_6);
            fn2 = (TextView) itemView.findViewById(R.id.custom_field_5);
            fn3 = (TextView) itemView.findViewById(R.id.custom_field_4);







        }

        public void bind(final FilterUtils item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


}