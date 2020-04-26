package com.example.getitright.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import com.example.getitright.db.repositories.listeners.OnGetAnswersForQuestionAnswerRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnInsertAllCategoryRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnInsertManyAnswerRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnInsertManyQuestionRepositoryActionListener;
import com.example.getitright.db.repositories.listeners.OnSelectAllCategoryRepositoryActionListener;
import com.example.getitright.fragments.QuestionFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private Integer categoryId;
    private String categoryName;
    ProgressDialog progressDialog;
    QuestionRepository questionRepository;
    AnswerRepository answerRepository;
    private Integer currentScore;
    private Integer currentQuestionIndex;
    private List<Question> questions;
    private FragmentManager fragmentManager;
    private Integer numberOfQuestions;
    private Integer indexOfCorrectAnswer;

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
        fragmentManager = getSupportFragmentManager();

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

                        question.text = new String(Base64.decode(questionJSON.getString("question"), Base64.DEFAULT));
                        question.categoryId = categoryId;
                        questions.add(question);

                        Answer answer = new Answer();
                        answer.text = new String(Base64.decode(questionJSON.getString("correct_answer"), Base64.DEFAULT));
                        answer.isCorrect = true;
                        answer.question_name = question.text;
                        answers.add(answer);

                        JSONArray incorrectAnswers = questionJSON.getJSONArray("incorrect_answers");
                        for(int i = 0; i < 3; i++){
                            Answer incorrectAnswer = new Answer();
                            incorrectAnswer.isCorrect = false;
                            incorrectAnswer.text = new String(Base64.decode(incorrectAnswers.getString(i), Base64.DEFAULT));
                            incorrectAnswer.question_name = question.text;
                            answers.add(incorrectAnswer);
                        }

                    } catch (JSONException ex) {
                        System.out.println("Exception when parsing questions json");
                        return;
                    }
                }
                GameActivity.this.questions = questions;
                numberOfQuestions = questions.size();

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
                        startGameFlow();
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
                    numberOfQuestions = questions.size();
                    GameActivity.this.questions = questions;
                    startGameFlow();
                }else{
                    Toast toast = Toast.makeText(GameActivity.this,
                            "No internet connection", Toast.LENGTH_LONG);
                    toast.show();
                }
            };
        }, categoryId);
    }

    protected void startGameFlow(){
        currentScore = 0;
        currentQuestionIndex = 1;
        final String questionName = questions.get(currentQuestionIndex).text;

        answerRepository.getAnswersForQuestionTask(new OnGetAnswersForQuestionAnswerRepositoryActionListener() {
            @Override
            public void actionSuccess(List<Answer> answers) {

                Collections.shuffle(answers);

                for(int i = 0; i < answers.size(); i++){
                    if(answers.get(i).isCorrect)
                    {
                        indexOfCorrectAnswer = i;
                        break;
                    }
                }

                QuestionFragment questionFragment = QuestionFragment.newInstance(currentScore, currentQuestionIndex, numberOfQuestions,
                        questionName, answers.stream().map(answer -> answer.text).toArray(String[]::new));

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.mainLayout, questionFragment , "fragment-question");
                fragmentTransaction.commitNow();
                addAnswerClickHandlers();
            }
        }, questionName);
    }

    protected void addAnswerClickHandlers(){
        Button answerBtn1 = this.findViewById(R.id.answerBtn1);
        Button answerBtn2 = this.findViewById(R.id.answerBtn2);
        Button answerBtn3 = this.findViewById(R.id.answerBtn3);
        Button answerBtn4 = this.findViewById(R.id.answerBtn4);
        Button[] buttons = {answerBtn1, answerBtn2, answerBtn3, answerBtn4};
        for (Integer i = 0; i < buttons.length; i++) {
            Button button = buttons[i];
            Integer finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateAnswer(finalI);
                }
            });
        }
    }

    protected void endGame(){
        System.out.println("YOUR SCORE IS: " + currentScore);
    }

    protected void validateAnswer(Integer clickedAnswerButtonIndex){
        currentQuestionIndex++;
        if(currentQuestionIndex >= numberOfQuestions)
        {
            endGame();
            return;
        }

        if(clickedAnswerButtonIndex.equals(indexOfCorrectAnswer)){
            currentScore++;
        }

        final String questionName = questions.get(currentQuestionIndex).text;

        answerRepository.getAnswersForQuestionTask(new OnGetAnswersForQuestionAnswerRepositoryActionListener() {
            @Override
            public void actionSuccess(List<Answer> answers) {
                Collections.shuffle(answers);

                for(int i = 0; i < answers.size(); i++){
                    if(answers.get(i).isCorrect)
                    {
                        indexOfCorrectAnswer = i;
                        break;
                    }
                }

                QuestionFragment questionFragment = QuestionFragment.newInstance(currentScore, currentQuestionIndex, numberOfQuestions,
                        questionName, answers.stream().map(answer -> answer.text).toArray(String[]::new));

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainLayout, questionFragment , "fragment-question");
                fragmentTransaction.commitNow();
                addAnswerClickHandlers();
            }
        }, questionName);
    }

}
