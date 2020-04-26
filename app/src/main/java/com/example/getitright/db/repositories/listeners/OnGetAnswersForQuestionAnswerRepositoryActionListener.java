package com.example.getitright.db.repositories.listeners;

import com.example.getitright.db.entities.Answer;

import java.util.List;

public interface OnGetAnswersForQuestionAnswerRepositoryActionListener {
    void actionSuccess(List<Answer> answers);
}
