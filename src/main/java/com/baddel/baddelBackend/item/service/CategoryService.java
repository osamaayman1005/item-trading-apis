package com.baddel.baddelBackend.item.service;

import com.baddel.baddelBackend.exception.ApiException;
import com.baddel.baddelBackend.global.DummyItemLinks;
import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.item.dtos.CategoryDTO;
import com.baddel.baddelBackend.item.models.Category;
import com.baddel.baddelBackend.item.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryDTO> getAllCategories(Boolean includeChildren) {

            return categoryRepository.findAllByParentIsNullOrderByName().stream()
                    .map(category -> category.toCategoryDto(includeChildren)).toList();
    }

    public CategoryDTO getCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(()->new ApiException("category not found"
                , HttpStatus.NOT_FOUND)).toCategoryDto(true);
    }

    public CategoryDTO saveCategory(CategoryDTO categoryDTO) {
        verifyCategoryOnSave(categoryDTO);
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        category.setMedia(new Media(DummyItemLinks.getRandomMedia(), ""));
        if(categoryDTO.getParentId() != null){
            category.setParent(
                    categoryRepository.findById(categoryDTO.getParentId()).orElseThrow(
                            () -> new ApiException("parent category not found", HttpStatus.BAD_REQUEST)
                    )
            );
        }
        try {
            return categoryRepository.save(category).toCategoryDto(true);

        } catch (DataIntegrityViolationException e){
            throw new ApiException("Category already exists", HttpStatus.BAD_REQUEST);
        }
    }
    public CategoryDTO updateCategory (CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.getId()).orElseThrow(
                () -> new ApiException("category not found", HttpStatus.BAD_REQUEST)
        );
        verifyCategoryOnUpdate(categoryDTO);
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        //TODO
        category.setMedia(new Media(DummyItemLinks.getRandomMedia(), ""));
        if(categoryDTO.getParentId() != null){
            category.setParent(
                    categoryRepository.findById(categoryDTO.getParentId()).orElseThrow(
                            () -> new ApiException("parent category not found", HttpStatus.BAD_REQUEST)
                    )
            );
        }
        try {
            return categoryRepository.save(category).toCategoryDto(true);

        } catch (DataIntegrityViolationException e){
            throw new ApiException("Category already exists", HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public void verifyCategoryOnSave(CategoryDTO category) {
        if (category.getParentId() == null) {
            categoryRepository.findByName(category.getName()).ifPresent(category1 -> {
                throw new ApiException("category: '" + category1.getName() + "' already exists",
                        HttpStatus.BAD_REQUEST);
            });
        } else {
            Category parent = categoryRepository.findById(category.getParentId()).get();
            categoryRepository.findByNameAndParent(category.getName(), parent).ifPresent(category1 -> {
                throw new ApiException("subcategory: '" + category1.getName()
                        + "' already exists under parent category: '" + parent.getName() + "' ",
                        HttpStatus.BAD_REQUEST);
            });
        }
    }
    public void verifyCategoryOnUpdate(CategoryDTO category) {
        if (category.getParentId() == null) {
            categoryRepository.findByName(category.getName()).ifPresent(category1 -> {
                if(!category.getId().equals(category1.getId()))
                throw new ApiException("category: '" + category1.getName() + "' already exists",
                        HttpStatus.BAD_REQUEST);
            });
        } else {
            Category parent = categoryRepository.findById(category.getParentId()).get();
            categoryRepository.findByNameAndParent(category.getName(), parent).ifPresent(category1 -> {
                if(!category.getId().equals(category1.getId()))
                throw new ApiException("subcategory: '" + category1.getName()
                        + "' already exists under parent category: '" + parent.getName() + "' ",
                        HttpStatus.BAD_REQUEST);
            });
        }
    }
}
