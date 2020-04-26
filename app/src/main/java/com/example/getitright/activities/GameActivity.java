package com.example.getitright.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.getitright.R;
import com.example.getitright.RequestHelper;
import com.example.getitright.db.entities.Answer;
import com.example.getitright.db.entities.Category;
import com.example.getitright.db.entities.Question;
import com.example.getitright.db.repositories.AnswerRepository;
import com.example.getitright.db.repositories.QuestionRepository;
import com.example.getitright.db.repositories.listeners.OnDeleteAllCategoryRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnDeleteAllFromCategoryAnswerRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnDeleteAllFromCategoryQuestionRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnGetAllFromCategoryQuestionRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnInsertAllCategoryRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnInsertManyAnswerRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnInsertManyQuestionRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnSelectAllCategoryRepositoryActionListener;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private Integer categoryId;
    private String categoryName;
    ProgressDialog progressDialog;
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Bundle b = getIntent().getExtras();
        if(b != null){
            categoryId = b.getInt("id");

            categoryName = b.getString("name");
        }
        initProgressDialog();
        actionBarSetup();

        questionRepository = new QuestionRepository(this);
        answerRepository = new AnswerRepository(this);

        getQuestionsForCategory();
    }

    protected void initProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setTitle("Getting Questions");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
    }

    protected void actionBarSetup() {
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle(categoryName);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void getQuestionsForCategory() {
        progressDialog.show();

        String src = String.format(getString(R.string.questions_for_category_url), categoryId);
        System.out.println("FETCHING FROM " + src);

        JsonObjectRequest request = new JsonObjectRequest(src,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                List<Question> questions = new ArrayList<>();
                List<Answer> answers = new ArrayList<>();
                JSONArray results;

                try {
                    results = response.getJSONArray("results");
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                }

                for (int index = 0; index < results.length(); index++) {
                    try {
                        Question question = new Question();
                        JSONObject questionJSON = results.getJSONObject(index);

                        question.text = questionJSON.getString("question");
                        question.categoryId = categoryId;
                        questions.add(question);

                        Answer answer = new Answer();
                        answer.text = questionJSON.getString("correct_answer");
                        answer.isCorrect = true;
                        answer.question_name = question.text;
                        answers.add(answer);

                        JSONArray incorrectAnswers = questionJSON.getJSONArray("incorrect_answers");
                        for(int i = 0; i < 3; i++){
                            Answer incorrectAnswer = new Answer();
                            incorrectAnswer.isCorrect = false;
                            incorrectAnswer.text = incorrectAnswers.getString(i);
                            incorrectAnswer.question_name = question.text;
                            answers.add(incorrectAnswer);
                        }

                    } catch (JSONException ex) {
                        System.out.println("Exception when parsing questions json");
                        return;
                    }
                }
                System.out.println(questions.toString());

                updateQuestionsInDb(questions, answers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getQuestionsFromDb();
            }
        });

        RequestHelper.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    protected void updateQuestionsInDb(final List<Question> questions, final List<Answer> answers) {
        questionRepository.deleteAllFromCategoryTask(new OnDeleteAllFromCategoryQuestionRepositoryActionListener() {
            @Override
            public void actionSuccess(){
                questionRepository.insertManyTask(new OnInsertManyQuestionRepositoryActionListener() {
                    @Override
                    public void actionSuccess(){
                        updateAnswersInDb(answers);
                    };
                }, questions.toArray(new Question[0]));
            };
        }, categoryId);
    }

    protected void updateAnswersInDb(final List<Answer> answers){
        answerRepository.deleteAllFromCategoryTask(new OnDeleteAllFromCategoryAnswerRepositoryActionListener() {
            @Override
            public void actionSuccess() {
                answerRepository.insertManyTask(new OnInsertManyAnswerRepositoryActionListener() {
                    @Override
                    public void actionSuccess() {
                        progressDialog.dismiss();
                    }
                }, answers.toArray(new Answer[0]));
            }
        }, categoryId);
    }

    protected void getQuestionsFromDb() {
        questionRepository.getAllFromCategoryTask(new OnGetAllFromCategoryQuestionRepositoryActionListener() {
            @Override
            public void actionSuccess(List<Question> questions){
                progressDialog.dismiss();

                if(questions.size() > 0){
                    System.out.println("DIN BD: " + questions.toString());
                }else{
                    Toast toast = Toast.makeText(GameActivity.this,
                            "No internet connection", Toast.LENGTH_LONG);
                    toast.show();
                }
            };
        }, categoryId);
    }

}
