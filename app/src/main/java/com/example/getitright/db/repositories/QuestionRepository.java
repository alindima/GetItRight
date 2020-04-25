package com.example.getitright.db.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.example.getitright.ApplicationController;
import com.example.getitright.db.AppDatabase;
import com.example.getitright.db.entities.Question;
import com.example.getitright.db.repositories.listeners.*;
import java.util.List;

public class QuestionRepository {
    private AppDatabase appDatabase;

    public QuestionRepository(Context context) {
        appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertManyTask(final OnInsertManyQuestionRepositoryActionListener listener,
                              Question... questions) {
        new InsertManyTask(listener).execute(questions);
    }

    public void deleteAllFromCategoryTask(final OnDeleteAllFromCategoryQuestionRepositoryActionListener listener, Integer categoryId) {
        new DeleteAllFromCategoryTask(listener).execute(categoryId);
    }

    public void getAllFromCategoryTask(final OnGetAllFromCategoryQuestionRepositoryActionListener listener, Integer categoryId) {
        new GetAllFromCategoryTask(listener).execute(categoryId);
    }

    private class InsertManyTask extends AsyncTask<Question, Void, Void> {
        OnInsertManyQuestionRepositoryActionListener listener;
        InsertManyTask(OnInsertManyQuestionRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Question... questions) {
            appDatabase.questionDao().insertMany(questions);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listener.actionSuccess();
        }
    }

    private class DeleteAllFromCategoryTask extends AsyncTask<Integer, Void, Void> {
        OnDeleteAllFromCategoryQuestionRepositoryActionListener listener;
        DeleteAllFromCategoryTask(OnDeleteAllFromCategoryQuestionRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Integer... categoryIds) {
            for (Integer categoryId : categoryIds) {
                appDatabase.questionDao().deleteAllFromCategory(categoryId);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listener.actionSuccess();
        }
    }

    private class GetAllFromCategoryTask extends AsyncTask<Integer, Void, List<Question>> {
        OnGetAllFromCategoryQuestionRepositoryActionListener listener;
        GetAllFromCategoryTask(OnGetAllFromCategoryQuestionRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Question> doInBackground(Integer ...categoryIds) {
            for (Integer categoryId : categoryIds) {
                List<Question> test = appDatabase.questionDao().getAllFromCategory(categoryId);
                return test;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Question> questions) {
            super.onPostExecute(questions);

            listener.actionSuccess(questions);
        }
    }
}
