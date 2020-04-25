package com.example.getitright.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.getitright.db.entities.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM question WHERE question.category_id = :categoryId")
    List<Question> getAllFromCategory(Integer categoryId);

    @Insert
    void insertMany(Question... questions);

    @Query("DELETE FROM question WHERE question.category_id = :categoryId")
    void deleteAllFromCategory(Integer categoryId);
}
