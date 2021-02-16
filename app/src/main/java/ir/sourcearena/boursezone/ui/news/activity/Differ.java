package ir.sourcearena.boursezone.ui.news.activity;


import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import ir.sourcearena.boursezone.ui.news.Utils;

public class Differ extends DiffUtil.Callback{

    List<Utils> oldPersons;
    List<Utils> newPersons;

    public Differ(List<Utils> newPersons, List<Utils> oldPersons) {
        this.newPersons = newPersons;
        this.oldPersons = oldPersons;
    }

    @Override
    public int getOldListSize() {
        return oldPersons.size();
    }

    @Override
    public int getNewListSize() {
        return newPersons.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPersons.get(oldItemPosition).getItem(0) == newPersons.get(newItemPosition).getItem(0);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldPersons.get(oldItemPosition).equals(newPersons.get(newItemPosition));
    }

    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}