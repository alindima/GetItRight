package com.example.getitright.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.getitright.R;
import com.example.getitright.db.entities.Category;

import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder> {
    private List<Category> mDataset;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {

        Button btnView;

        ViewHolder(View v) {
            super(v);

            btnView = v.findViewById(R.id.button);
        }
    }

    public CategoriesListAdapter(Context pContext, List<Category> myDataset) {
        context = pContext;
        mDataset = myDataset;
    }

    @Override
    public CategoriesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.btnView.setText(mDataset.get(position).name);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
