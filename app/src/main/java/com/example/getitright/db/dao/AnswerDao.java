package com.example.getitright.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.getitright.db.entities.Answer;

import java.util.List;

@Dao
public interface AnswerDao {
    @Query("SELECT * FROM answer")
    List<Answer> getAll();

    @Insert
    void insertAll(Answer... answers);

    @Delete
    void delete(Answer answer);
}
