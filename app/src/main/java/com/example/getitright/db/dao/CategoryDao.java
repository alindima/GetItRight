package com.example.getitright.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.getitright.db.entities.Category;

import java.util.List;

@Dao
public interface CategoryDao {
    @Query("SELECT * FROM category")
    List<Category> getAll();

    @Insert
    void insertAll(Category... categories);

    @Query("DELETE FROM category")
    void deleteAll();
}