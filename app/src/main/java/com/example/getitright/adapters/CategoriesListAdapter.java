package com.example.getitright.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.getitright.R;
import com.example.getitright.activities.GameActivity;
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
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = mDataset.get(position).id;
                String name = mDataset.get(position).name;
                launchGameActivity(id, name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    protected void launchGameActivity(Integer id, String name) {
        Intent intent = new Intent(context, GameActivity.class);
        Bundle b = new Bundle();
        b.putInt("id", id);
        b.putString("name", name);
        intent.putExtras(b);
        context.startActivity(intent);
    }
}
