package ir.sourcearena.boursezone.searchHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import id.voela.actrans.AcTrans;
import ir.sourcearena.boursezone.NamadRouter;
import ir.sourcearena.boursezone.Settings;
import ir.sourcearena.boursezone.R;

import androidx.recyclerview.widget.RecyclerView;

import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;

public class CustomSuggestionsAdapter extends SuggestionsAdapter<Namad, CustomSuggestionsAdapter.SuggestionHolder> {
    LayoutInflater inf = null;
    Context ctx;

    public CustomSuggestionsAdapter(LayoutInflater inflater, Context ctx) {
        super(inflater);
        this.inf = inflater;
        this.ctx= ctx;
    }





    @Override
    public int getSingleViewHeight() {
        return 80;
    }

    @Override
    public SuggestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.search_view, parent, false);
        return new SuggestionHolder(view);
    }

    @Override
    public void onBindSuggestionHolder(final Namad suggestion, SuggestionHolder holder, final int position) {
        holder.name.setText(suggestion.getName());
        holder.full_name.setText(suggestion.getFname());
        holder.market.setText(suggestion.getMarket());
       (holder.itemView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ctx, NamadRouter.class);
                in.putExtra(Settings.TITLE_EXTRA,suggestions.get(position).getName());


                ((Activity) ctx).startActivityForResult(in,10);
                new AcTrans.Builder(ctx).performFade();
            }
        });


    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                String term = constraint.toString();
                if(term.isEmpty())
                    suggestions = suggestions_clone;
                else {
                    suggestions = new ArrayList<>();
                    for (Namad item: suggestions_clone)
                        if(item.getName().contains(term))
                            suggestions.add(item);
                }
                results.values = suggestions;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                suggestions = (ArrayList<Namad>) results.values;
                notifyDataSetChanged();
            }
        };
    }



    static class SuggestionHolder extends RecyclerView.ViewHolder{
        protected TextView name, full_name, market;


        public SuggestionHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.searched_name);
            full_name = (TextView) itemView.findViewById(R.id.searched_full_name);
            market = (TextView) itemView.findViewById(R.id.searched_market);

        }
    }

}