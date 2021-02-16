package ir.sourcearena.boursezone.technical.helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.R;


public class RecyclerAdapter2 extends RecyclerView.Adapter<RecyclerAdapter2.ViewHolder> {

    private Context context;
    private List<Utils2> utils;
    Settings setting;
    Drawable d_active;
    Drawable active;
    public RecyclerAdapter2(Context context, List utils) {
        this.context = context;
        this.utils = utils;
        active = ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_check_24,null);
        d_active = ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_baseline_close_24,null);
        d_active.setTint(ResourcesCompat.getColor(context.getResources(),R.color.red,null));

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_indicator_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    Utils2 pu;
    boolean reverse;

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        setting = new Settings();
        holder.itemView.setTag(utils.get(position));

        holder.setIsRecyclable(false);
         pu = utils.get(position);


        holder.name.setText(pu.getName());
        holder.active.setText(pu.getActive());
        if(position ==utils.size()-1){
            holder.divider.setVisibility(View.GONE);
        }



    }


    @Override
    public int getItemCount() {
        return utils.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{



        public TextView name;
        public TextView active;
        public View divider;


        public ViewHolder(View itemView) {
            super(itemView);



            name =  itemView.findViewById(R.id.ind_name);
            active =  itemView.findViewById(R.id.ind_val);
            divider =  itemView.findViewById(R.id.divider_first);





        }


    }


}