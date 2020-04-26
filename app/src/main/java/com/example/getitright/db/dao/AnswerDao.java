package com.example.getitright.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.getitright.db.entities.Answer;

import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM answer WHERE question_name = :questionName")
    List<Answer> getAnswersForQuestion(String questionName);

    @Insert
    void insertMany(Answer... answers);

    @Query("DELETE FROM answer WHERE question_name in (SELECT text from question where category_id = :categoryId)")
    void deleteFromCategory(Integer categoryId);

}
