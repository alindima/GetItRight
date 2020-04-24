package com.example.getitright.db.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.example.getitright.ApplicationController;
import com.example.getitright.db.AppDatabase;
import com.example.getitright.db.entities.Category;
import com.example.getitright.db.repositories.listeners.OnDeleteAllCategoryRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnInsertAllCategoryRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnSelectAllCategoryRepositoryActionListener;

import java.util.List;

public class CategoryRepository {
    private AppDatabase appDatabase;

    public CategoryRepository(Context context) {
        appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertAllTask(final OnInsertAllCategoryRepositoryActionListener listener,
                              Category... categories) {
        new InsertAllTask(listener).execute(categories);
    }

    public void deleteAllTask(final OnDeleteAllCategoryRepositoryActionListener listener) {
        new DeleteAllTask(listener).execute();
    }

    public void selectAllTask(final OnSelectAllCategoryRepositoryActionListener listener) {
        new SelectAllTask(listener).execute();
    }

    private class InsertAllTask extends AsyncTask<Category, Void, Void> {
        OnInsertAllCategoryRepositoryActionListener listener;
        InsertAllTask(OnInsertAllCategoryRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Category... categories) {
            appDatabase.categoryDao().insertAll(categories);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listener.actionSuccess();
        }
    }

    private class DeleteAllTask extends AsyncTask<Void, Void, Void> {
        OnDeleteAllCategoryRepositoryActionListener listener;
        DeleteAllTask(OnDeleteAllCategoryRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDatabase.categoryDao().deleteAll();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listener.actionSuccess();
        }
    }

    private class SelectAllTask extends AsyncTask<Void, Void, List<Category>> {
        OnSelectAllCategoryRepositoryActionListener listener;
        SelectAllTask(OnSelectAllCategoryRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Category> doInBackground(Void... voids) {
            return appDatabase.categoryDao().getAll();
        }

        @Override
        protected void onPostExecute(List<Category> categories) {
            super.onPostExecute(categories);

            listener.actionSuccess(categories);
        }
    }
}
