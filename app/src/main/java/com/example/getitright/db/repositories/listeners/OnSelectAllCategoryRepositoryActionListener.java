package com.example.getitright.db.repositories.listeners;

import com.example.getitright.db.entities.Category;

import java.util.List;

public interface OnSelectAllCategoryRepositoryActionListener {
    void actionSuccess(List<Category> categories);
}
