package com.blogapp.services.impl;

import com.blogapp.enums.ErrorCodes;
import com.blogapp.exceptions.BusinessException;
import com.blogapp.model.RequestPage;
import com.blogapp.model.dao.Category;
import com.blogapp.model.dto.CategoryDto;
import com.blogapp.repository.CategoryRepository;
import com.blogapp.services.CategoryService;
import com.blogapp.utils.CommonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    public final CategoryRepository categoryRepository;
    public final ObjectMapper objectMapper;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = objectMapper.convertValue(categoryDto, Category.class);
        category = categoryRepository.save(category);
        log.info("------CategoryServiceImpl:addCategory:: new category is been added with id: {} -> title: {}", category.getId(), category.getTitle());
        return objectMapper.convertValue(category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategory(long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCodes.CATEGORY_NOT_FOUND, HttpStatus.BAD_REQUEST));

        log.info("------CategoryServiceImpl:updateCategory-Before update Category : title: {} , description: {}------", category.getTitle(), category.getDescription());
        Category updatedResource = objectMapper.convertValue(categoryDto, Category.class);
        BeanUtils.copyProperties(updatedResource,category, CommonUtils.getNullPropertyNames(updatedResource));
        log.info("------CategoryServiceImpl:updateCategory-After update Category : title: {} , description: {}------", category.getTitle(), category.getDescription());
        category.setId(id);

        category = categoryRepository.save(category);
        return objectMapper.convertValue(category, CategoryDto.class);

    }

    @Override
    public void deleteCategory(long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isPresent()) {
            categoryRepository.deleteById(id);
            log.info("--------CategoryServiceImpl:deleteCategory:: category with id: {} deleted successfully----", id);
        }
        throw new BusinessException(ErrorCodes.CATEGORY_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }

    @Override
    public CategoryDto getCategoryById(long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (!category.isPresent()) throw new BusinessException(ErrorCodes.CATEGORY_NOT_FOUND, HttpStatus.BAD_REQUEST);
        log.info("--------CategoryServiceImpl:getCategoryById:: fetching category with id: {} ----", id);
        return objectMapper.convertValue(category.get(), CategoryDto.class);
    }

    @Override
    public List<CategoryDto> getAllCategory(RequestPage pageRequest) {
        Sort sort = (pageRequest.getSortDir().equalsIgnoreCase("asc")) ? Sort.by(pageRequest.getSortBy()).ascending() : Sort.by(pageRequest.getSortBy()).descending();
        Pageable page = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
        Page<Category> categoriesPage = categoryRepository.findAll(page);
        List<Category> categoryList = categoriesPage.getContent();
        return categoryList.stream().map(category -> objectMapper.convertValue(category, CategoryDto.class)).collect(Collectors.toList());
    }
}
