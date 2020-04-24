package com.example.getitright.db.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "name")
    public String name;
}
