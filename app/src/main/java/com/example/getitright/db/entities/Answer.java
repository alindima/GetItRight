package com.example.getitright.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = Question.class,
        parentColumns = "id",
        childColumns = "question_id",
        onDelete = ForeignKey.CASCADE))
public class Answer {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "is_correct")
    public boolean isCorrect;

    public int question_id;
}
