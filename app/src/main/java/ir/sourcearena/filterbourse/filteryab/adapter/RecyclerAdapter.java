package ir.sourcearena.filterbourse.filteryab.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sourcearena.filterbourse.R;
import ir.sourcearena.filterbourse.Settings;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private List<FilterUtils> utils;
    Settings setting;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(FilterUtils item, View remove, TextView tv);
    }

    public RecyclerAdapter(Context context, List utils, OnItemClickListener listener) {
        this.context = context;
        this.utils = utils;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_filter, parent, false);
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
        holder.count.setText(pu.getCount());

        holder.bind(pu, listener);

    }

    @Override
    public int getItemCount() {
        return utils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, count;
        ImageView remove;


        public ViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.filter_name);
            count = (TextView) itemView.findViewById(R.id.textView5);
            remove =  itemView.findViewById(R.id.imageView5);
            remove.setClickable(true);
            itemView.setClickable(true);
        }

        public void bind(final FilterUtils item, final OnItemClickListener listener) {

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        listener.onItemClick(item,remove,name);



                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(item,itemView,name);
                    lastView = name;



                }
            });

        }
    }
    TextView lastView;
   public View getLastName(){
        return lastView;
   }


}