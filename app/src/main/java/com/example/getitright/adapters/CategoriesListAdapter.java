package com.example.getitright.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.ViewHolder> {
    private List<Category> mDataset;
    private Context context;

    private static Map<Integer, Integer> btnColorMap = new HashMap<>();

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

        btnColorMap.put(0, R.color.color0);
        btnColorMap.put(1, R.color.color1);
        btnColorMap.put(2, R.color.color2);
        btnColorMap.put(3, R.color.color3);
        btnColorMap.put(4, R.color.color4);
        btnColorMap.put(5, R.color.color5);
        btnColorMap.put(6, R.color.color6);
    }

    @Override
    public CategoriesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);

        return new ViewHolder(v);
    }

    private static int getRandomColorIndex() {
        int min = 0, max = 6;

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.btnView.setText(mDataset.get(position).name);
        holder.btnView.setBackgroundResource(btnColorMap.get(getRandomColorIndex()));

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
