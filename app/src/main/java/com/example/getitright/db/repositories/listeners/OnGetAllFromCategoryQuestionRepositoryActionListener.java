package com.example.getitright.db.repositories.listeners;

import com.example.getitright.db.entities.Question;

import java.util.List;

public interface OnGetAllFromCategoryQuestionRepositoryActionListener {
    void actionSuccess(List<Question> questions);
}
