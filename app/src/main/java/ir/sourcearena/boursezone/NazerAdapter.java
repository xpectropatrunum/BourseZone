package ir.sourcearena.boursezone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class NazerAdapter extends RecyclerView.Adapter<NazerAdapter.ViewHolder> {

    private Context context;
    private List<FilterUtils> utils;
    Settings setting;


    public NazerAdapter(Context context, List<FilterUtils> utils) {
        this.context = context;
        this.utils = utils;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_nazer_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    FilterUtils pu;
    int mExpandedPosition = -1;
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setting = new Settings();
        holder.itemView.setTag(utils.get(position));

        pu = utils.get(position);
        final Settings setting = new Settings();



        holder.title.setText(pu.getIndustry());
        holder.date.setText(pu.getF1());
        holder.text.setText(pu.getF2());


        holder.date.setTextSize(setting.CARD_FONT_SIZE);
        holder.title.setTextSize(setting.CARD_FONT_SIZE);
        holder.text.setTextSize(setting.CARD_FONT_SIZE-1);




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


        public TextView text;
        public TextView date;
        public TextView title;




        public ViewHolder(View itemView) {
            super(itemView);


            date = (TextView) itemView.findViewById(R.id.nazer_date);
            title = (TextView) itemView.findViewById(R.id.nazer_title);
            text = (TextView) itemView.findViewById(R.id.nazer_text);




        }


    }


}