package com.example.getitright.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
import com.example.getitright.db.entities.Category;
import com.example.getitright.db.repositories.CategoryRepository;
import com.example.getitright.db.repositories.listeners.OnDeleteAllCategoryRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnInsertAllCategoryRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnSelectAllCategoryRepositoryActionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView categoryRecyclerView;
    ProgressDialog progressDialog;
    CategoryRepository categoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        initProgressDialog();
        actionBarSetup();

        categoryRepository = new CategoryRepository(CategoryActivity.this);

        initRecyclerView();
    }

    protected void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Getting Categories");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
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
        progressDialog.show();

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

                deleteCategoriesFromDb();
                insertCategoriesInDb(categories);

                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getCategoriesFromDb();

                progressDialog.dismiss();
            }
        });

        RequestHelper.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    protected void insertCategoriesInDb(List<Category> categories) {
        categoryRepository.insertAllTask(new OnInsertAllCategoryRepositoryActionListener() {
            @Override
            public void actionSuccess(){
            };
        }, categories.toArray(new Category[0]));
    }

    protected void deleteCategoriesFromDb() {
        categoryRepository.deleteAllTask(new OnDeleteAllCategoryRepositoryActionListener() {
            @Override
            public void actionSuccess(){
            };
        });
    }

    protected void getCategoriesFromDb() {
        categoryRepository.selectAllTask(new OnSelectAllCategoryRepositoryActionListener() {
            @Override
            public void actionSuccess(List<Category> categories){
                if(categories.size() > 0){
                    setCategoriesRecyclerViewData(categories);
                }else{
                    Toast toast = Toast.makeText(CategoryActivity.this,
                            "No internet connection", Toast.LENGTH_LONG);
                    toast.show();
                }
            };
        });
    }
}
