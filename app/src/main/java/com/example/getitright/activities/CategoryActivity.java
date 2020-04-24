package com.example.getitright.activities;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.getitright.R;
import com.example.getitright.RequestHelper;
import com.example.getitright.adapters.CategoriesListAdapter;
import com.example.getitright.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        actionBarSetup();
        initRecyclerView();
    }

    protected void actionBarSetup() {
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle(R.string.category_activity_title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void initRecyclerView() {
        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        categoryRecyclerView.setHasFixedSize(true);

        getCategories();
    }

    protected void setCategoriesRecyclerViewData(List<Category> categories){
        CategoriesListAdapter adapter = new CategoriesListAdapter(this, categories);
        categoryRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected void getCategories() {
        String src = getString(R.string.list_categories_url);

        JsonObjectRequest request = new JsonObjectRequest( src,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Category> categories = new ArrayList<>();
                JSONArray triviaCategories;

                try {
                    triviaCategories = response.getJSONArray("trivia_categories");
                } catch (JSONException e) {
                    e.printStackTrace();

                    return;
                }

                for (int index = 0; index < triviaCategories.length(); index++) {
                    try {
                        Category category = new Category();
                        JSONObject catJSON = triviaCategories.getJSONObject(index);

                        category.name = catJSON.getString("name");
                        category.id = catJSON.getInt("id");

                        categories.add(category);

                    } catch (JSONException ex) {
                        System.out.println("Exception when parsing json");

                        return;
                    }
                }

                setCategoriesRecyclerViewData(categories);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley error:" + error.getMessage());
            }
        });

        RequestHelper.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
