package ir.sourcearena.boursezone.ui.watcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sourcearena.boursezone.R;
import ir.sourcearena.boursezone.Settings;


public class WatcherAdapter extends RecyclerView.Adapter<WatcherAdapter.ViewHolder> {

    private Context context;
    private List<WUtils> utils;
    Settings setting;
    OnItemClickListener listener;
    OnItemLongClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(WUtils item);
    }
    public interface OnItemLongClickListener {



        void onItemLongClick(WUtils item);
    }
    public WatcherAdapter(Context context, List utils, OnItemClickListener listener, OnItemLongClickListener mlistener) {
        this.context = context;

        this.utils = utils;
        this.mlistener = mlistener;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_dideban_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    WUtils pu;

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        setting = new Settings();
        pu = utils.get(position);
        holder.itemView.setTag(utils.get(position));

        holder.name.setText(pu.getName());
        holder.count.setText(pu.getCount()+" نماد");







        holder.bind(pu, listener);

    }




    @Override
    public int getItemCount() {
        return utils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        public TextView name;
        public TextView count;




        public ViewHolder(View itemView) {
            super(itemView);

            count = (TextView) itemView.findViewById(R.id.sw_count);
            name = (TextView) itemView.findViewById(R.id.sw_name);








        }

        public void bind(final WUtils item, final OnItemClickListener listener) {
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