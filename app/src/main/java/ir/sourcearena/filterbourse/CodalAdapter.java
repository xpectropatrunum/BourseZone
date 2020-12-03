package ir.sourcearena.filterbourse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CodalAdapter extends RecyclerView.Adapter<CodalAdapter.ViewHolder> {

    private Context context;
    private List<FilterUtils> utils;
    Settings setting;
    OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(String  url);
    }

    public CodalAdapter(Context context, List<FilterUtils> utils, OnItemClickListener listener) {
        this.context = context;
        this.utils = utils;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_codal_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    FilterUtils pu;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setting = new Settings();
        holder.itemView.setTag(utils.get(position));

        pu = utils.get(position);
        final Settings setting = new Settings();

        holder.name.setText(pu.getName());
        holder.fullname.setText(pu.getMarket());
        holder.letter.setText(pu.getIndustry());
        holder.date.setText(pu.getF1());
        holder.title.setText(pu.getF2());
        if(pu.getF3().equals("")){
            holder.link1.setVisibility(View.GONE);
        }
        if(pu.getFn1().equals("")){
            holder.link2.setVisibility(View.GONE);
        }
        if(pu.getFn2().equals("")){
            holder.link3.setVisibility(View.GONE);
        }
        holder.name.setTextSize(setting.CARD_FONT_SIZE + 2);
        holder.fullname.setTextSize(setting.CARD_FONT_SIZE);
        holder.letter.setTextSize(setting.CARD_FONT_SIZE);
        holder.date.setTextSize(setting.CARD_FONT_SIZE);
        holder.title.setTextSize(setting.CARD_FONT_SIZE);


        holder.bind(pu, listener);

    }

    public static String PersianToEnglish(String persianStr) {
        String answer = persianStr;
        answer = answer.replace("۱", "1");
        answer = answer.replace("۲", "2");
        answer = answer.replace("۳", "3");
        answer = answer.replace("۴", "4");
        answer = answer.replace("۵", "5");
        answer = answer.replace("۶", "6");
        answer = answer.replace("۷", "7");
        answer = answer.replace("۸", "8");
        answer = answer.replace("۹", "9");
        answer = answer.replace("۰", "0");
        return answer;
    }

    @Override
    public int getItemCount() {
        return utils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView fullname;
        public TextView letter;
        public TextView date;
        public TextView title;
        public ImageView link1;
        public ImageView link2;
        public ImageView link3;


        public ViewHolder(View itemView) {
            super(itemView);

            fullname = (TextView) itemView.findViewById(R.id.codal_fullname);
            name = (TextView) itemView.findViewById(R.id.codal_name);
            letter = (TextView) itemView.findViewById(R.id.codal_letter);
            date = (TextView) itemView.findViewById(R.id.codal_date);
            title = (TextView) itemView.findViewById(R.id.codal_title);
            link1 = itemView.findViewById(R.id.l1);
            link2 = itemView.findViewById(R.id.l2);
            link3 = itemView.findViewById(R.id.l3);


        }

        public void bind(final FilterUtils item, final OnItemClickListener listener) {

            link1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item.getF3()+"");
                }
            });

                link2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item.getFn1()+"");
                    }
                });



                link3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(item.getFn2()+"");
                    }
                });

        }
    }


}