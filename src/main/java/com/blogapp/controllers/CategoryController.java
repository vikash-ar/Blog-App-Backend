package com.blogapp.controllers;

import com.blogapp.model.RequestPage;
import com.blogapp.model.dto.CategoryDto;
import com.blogapp.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.addCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable("id") long id, RequestPage pageRequest) {
        CategoryDto category = categoryService.getCategoryById(id);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategory(RequestPage pageRequest) {
        List<CategoryDto> categoryList = categoryService.getAllCategory(pageRequest);
        return new ResponseEntity<>(categoryList, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable("id") long id, @RequestBody CategoryDto categoryDto) {
        CategoryDto category = categoryService.updateCategory(id, categoryDto);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") long id) {
        categoryService.deleteCategory(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
