package com.example.getitright.db.repositories;

import android.content.Context;
import android.os.AsyncTask;

import com.example.getitright.ApplicationController;
import com.example.getitright.db.AppDatabase;
import com.example.getitright.db.entities.Answer;
import com.example.getitright.db.repositories.listeners.*;
import java.util.List;

public class AnswerRepository {
    private AppDatabase appDatabase;

    public AnswerRepository(Context context) {
        appDatabase = ApplicationController.getAppDatabase();
    }

    public void insertManyTask(final OnInsertManyAnswerRepositoryActionListener listener,
                               Answer... answers) {
        new InsertManyTask(listener).execute(answers);
    }

    public void deleteAllFromCategoryTask(final OnDeleteAllFromCategoryAnswerRepositoryActionListener listener, Integer categoryId) {
        new DeleteAllFromCategoryTask(listener).execute(categoryId);
    }

    public void getAnswersForQuestionTask(final OnGetAnswersForQuestionAnswerRepositoryActionListener listener, String questionName) {
        new GetAnswersForQuestionTask(listener).execute(questionName);
    }

    private class InsertManyTask extends AsyncTask<Answer, Void, Void> {
        OnInsertManyAnswerRepositoryActionListener listener;
        InsertManyTask(OnInsertManyAnswerRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Answer... answers) {
            appDatabase.answerDao().insertMany(answers);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listener.actionSuccess();
        }
    }

    private class DeleteAllFromCategoryTask extends AsyncTask<Integer, Void, Void> {
        OnDeleteAllFromCategoryAnswerRepositoryActionListener listener;
        DeleteAllFromCategoryTask(OnDeleteAllFromCategoryAnswerRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Integer... categoryIds) {
            for (Integer categoryId : categoryIds) {
                appDatabase.answerDao().deleteFromCategory(categoryId);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            listener.actionSuccess();
        }
    }

    private class GetAnswersForQuestionTask extends AsyncTask<String, Void, List<Answer>> {
        OnGetAnswersForQuestionAnswerRepositoryActionListener listener;
        GetAnswersForQuestionTask(OnGetAnswersForQuestionAnswerRepositoryActionListener listener) {
            this.listener = listener;
        }

        @Override
        protected List<Answer> doInBackground(String ...questionNames) {
            for (String questionName : questionNames) {
                List<Answer> result = appDatabase.answerDao().getAnswersForQuestion(questionName);
                return result;
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Answer> answers) {
            super.onPostExecute(answers);
            listener.actionSuccess(answers);
        }
    }
}
