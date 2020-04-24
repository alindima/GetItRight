package com.example.getitright.db.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.example.getitright.ApplicationController;
import com.example.getitright.db.AppDatabase;
import com.example.getitright.db.entities.Question;

public class QuestionRepository {
    private AppDatabase appDatabase;

    public QuestionRepository(Context context) {
        appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertTask(final Question question,
                           final OnQuestionRepositoryActionListener listener) {
        new InsertTask(listener).execute(question);
    }

    private class InsertTask extends AsyncTask<Question, Void, Void> {
        OnQuestionRepositoryActionListener listener;
        InsertTask(OnQuestionRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Question... users) {

            //appDatabase.questionDao().insertTask(users[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listener.actionSuccess();
        }
    }
}

interface OnQuestionRepositoryActionListener {
    void actionSuccess();
    void actionFailed();
}
