package com.example.getitright.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.getitright.db.dao.AnswerDao;
import com.example.getitright.db.dao.CategoryDao;
import com.example.getitright.db.dao.QuestionDao;
import com.example.getitright.db.entities.Answer;
import com.example.getitright.db.entities.Category;
import com.example.getitright.db.entities.Question;

@Database(entities = {Question.class, Answer.class, Category.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuestionDao questionDao();
    public abstract AnswerDao answerDao();
    public abstract CategoryDao categoryDao();
}