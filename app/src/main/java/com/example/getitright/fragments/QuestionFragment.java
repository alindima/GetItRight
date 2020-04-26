package com.example.getitright.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.getitright.R;

public class QuestionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";


    private Integer currentScore;
    private Integer currentQuestionIndex;
    private Integer numberOfQuestions;
    private String questionText;
    private String[] answersList;

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(Integer currentScore, Integer currentQuestionIndex, Integer numberOfQuestions,
                                               String questionText, String[] answersList) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, currentScore);
        args.putInt(ARG_PARAM2, currentQuestionIndex);
        args.putInt(ARG_PARAM3, numberOfQuestions);
        args.putString(ARG_PARAM4, questionText);
        args.putStringArray(ARG_PARAM5, answersList);

        fragment.setArguments(args);
        return fragment;
    }

    private void updateContent(View view){

        TextView questionTextView = view.findViewById(R.id.questionTextView);
        questionTextView.setText(questionText);
        Button answerBtn1 = view.findViewById(R.id.answerBtn1);
        Button answerBtn2 = view.findViewById(R.id.answerBtn2);
        Button answerBtn3 = view.findViewById(R.id.answerBtn3);
        Button answerBtn4 = view.findViewById(R.id.answerBtn4);
        answerBtn1.setText(answersList[0]);
        answerBtn2.setText(answersList[1]);
        answerBtn3.setText(answersList[2]);
        answerBtn4.setText(answersList[3]);

        TextView scoreTextView = view.findViewById(R.id.scoreTextView);
        scoreTextView.setText("SCORE: " + currentScore);

        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(numberOfQuestions);
        progressBar.setProgress(currentQuestionIndex);

        return;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentScore = getArguments().getInt(ARG_PARAM1);
            currentQuestionIndex = getArguments().getInt(ARG_PARAM2);
            numberOfQuestions = getArguments().getInt(ARG_PARAM3);
            questionText = getArguments().getString(ARG_PARAM4);
            answersList = getArguments().getStringArray(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        updateContent(view);
        return view;
    }

}
