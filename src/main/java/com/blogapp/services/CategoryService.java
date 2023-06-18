package com.blogapp.services;

import com.blogapp.model.RequestPage;
import com.blogapp.model.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(CategoryDto category);
    CategoryDto updateCategory(long id, CategoryDto category);
    void deleteCategory(long id);
    CategoryDto getCategoryById(long id);
    List<CategoryDto> getAllCategory(RequestPage pageRequest);
}
