package ir.sourcearena.filterbourse.ui.firstfilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sourcearena.filterbourse.ui.firstfilter.FilterUtils;
import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;


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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.first_filter_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    FilterUtils pu;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position == 4){
            holder.d.setVisibility(View.GONE);
        }
        setting = new Settings();
        holder.itemView.setTag(utils.get(position));

         pu = utils.get(position);
        final Settings setting = new Settings();


        holder.f1.setText(pu.getF1());
        holder.f2.setText(pu.getF2());
        holder.f3.setText(pu.getF3());
        holder.f4.setText(pu.getF4());
        holder.f1.setTextSize(setting.CARD_FONT_SIZE);
        holder.f2.setTextSize(setting.CARD_FONT_SIZE);
        holder.f3.setTextSize(setting.CARD_FONT_SIZE);
        holder.f4.setTextSize(setting.CARD_FONT_SIZE);



        checkPercent(holder.f1);
        checkPercent(holder.f2);
        checkPercent(holder.f3);
        checkPercent(holder.f4);

        holder.bind(pu, listener);

    }
    private void checkPercent(TextView tv)  throws NumberFormatException {
        String text = tv.getText().toString();
        text = PersianToEnglish(text);

        if(text.contains("%")){
            Float percent = Float.parseFloat(text.replace("%",""));

            if(percent > 0){
                tv.setTextColor(context.getResources().getColor(R.color.green));
            }else{
                tv.setTextColor(context.getResources().getColor(R.color.red));
            }
        }
       else if(text.contains("-") || text.contains("+")){
            Float percent = 0f;
            if(text.contains(",")){
                percent = Float.parseFloat(text.replace(",",""));
            }
            else if(text.contains("M")){
                percent = Float.parseFloat(text.replace("M",""));
            }else if(text.contains("B")) {
                percent = Float.parseFloat(text.replace("B", ""));
            }else if(text.contains("K")) {
                percent = Float.parseFloat(text.replace("K", ""));
            }


            if(percent > 0){
                tv.setTextColor(context.getResources().getColor(R.color.green));
            }else{
                tv.setTextColor(context.getResources().getColor(R.color.red));
            }
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



        public TextView f1;
        public TextView f2;
        public TextView f3;
        public TextView f4;
        public View d;

        public ViewHolder(View itemView) {
            super(itemView);


            f1 = (TextView) itemView.findViewById(R.id.namad_full_name);
            f2 = (TextView) itemView.findViewById(R.id.custom_field22);
            f3 = (TextView) itemView.findViewById(R.id.close_price);
            f4 = (TextView) itemView.findViewById(R.id.price_change_percent);
            d =itemView.findViewById(R.id.divider_first);







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