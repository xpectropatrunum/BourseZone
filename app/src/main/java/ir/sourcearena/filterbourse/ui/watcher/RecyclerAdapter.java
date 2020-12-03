package ir.sourcearena.filterbourse.ui.watcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Utils> utils;
    Settings setting;
    OnItemClickListener listener;
    OnItemLongClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(Utils item);
    }
    public interface OnItemLongClickListener {



        void onItemLongClick(Utils item);
    }
    public RecyclerAdapter(Context context, List utils, OnItemClickListener listener, OnItemLongClickListener mlistener) {
        this.context = context;
        this.utils = utils;
        this.mlistener = mlistener;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_favorite_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    Utils pu;
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setting = new Settings();
        holder.itemView.setTag(utils.get(position));

         pu = utils.get(position);
        holder.name.setText(pu.getName());
        holder.full_name.setText(pu.getFName());
        holder.state.setText(pu.getState());
        holder.price.setText(pu.getPrice());
        holder.change.setText(pu.getChange());
        holder.percent.setText(pu.getPercent());

        //holder.full_name.setTextSize(setting.CARD_FONT_SIZE+2);



        checkPercent(holder.change);
        checkPercent(holder.percent);
        checkColor(holder.state);


        holder.bind(pu, listener);

    }

    private void checkColor(TextView tv) {
        if((tv.getText().toString()).contains("مجاز")){
            tv.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            tv.setTextColor(context.getResources().getColor(R.color.red));
        }
    }

    private void checkPercent(TextView tv)  {
        String text = tv.getText().toString();
        text = PersianToEnglish(text);

        if(!text.contains("-")){

                tv.setTextColor(context.getResources().getColor(R.color.green));
            }else{
                tv.setTextColor(context.getResources().getColor(R.color.red));

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
        public TextView name;
        public TextView state;
        public TextView price;
        public TextView change;
        public TextView percent;


        public ViewHolder(View itemView) {
            super(itemView);

            full_name = (TextView) itemView.findViewById(R.id.favorite_fullname);
            name = (TextView) itemView.findViewById(R.id.favorite_name);
            state = (TextView) itemView.findViewById(R.id.favorite_state);
            price = (TextView) itemView.findViewById(R.id.favorite_price);
            change = (TextView) itemView.findViewById(R.id.favorite_change);
            percent = (TextView) itemView.findViewById(R.id.favorite_percent);








        }

        public void bind(final Utils item, final OnItemClickListener listener) {
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mlistener.onItemLongClick(item);
                    return true;
                }


            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


}