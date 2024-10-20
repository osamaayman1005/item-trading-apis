package com.baddel.baddelBackend.item.controllers;

import com.baddel.baddelBackend.item.dtos.CategoryDTO;
import com.baddel.baddelBackend.item.service.CategoryService;
import com.baddel.baddelBackend.global.dtos.ResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("api/v1/public/categories")
    public ResponseDTO getAllCategories(@RequestParam(defaultValue = "false", required = false)
                                            Boolean includeSubCategories) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("categories retrieved successfully");
        responseDTO.setData(categoryService.getAllCategories(includeSubCategories));
        return responseDTO;

    }

    @GetMapping("api/v1/public/categories/{id}")
    public ResponseDTO getCategoryById(@PathVariable Long id) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("categories retrieved successfully");
        responseDTO.setData(categoryService.getCategoryById(id));

        return responseDTO;
    }

    @PostMapping("api/v1/admin/categories")
    public ResponseDTO createCategory(@RequestBody CategoryDTO category) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("categories created successfully");
        System.out.println(category);
        responseDTO.setData(categoryService.saveCategory(category));
        return responseDTO;
    }

    @PutMapping("api/v1/admin/categories")
    public ResponseDTO updateCategory(@RequestBody CategoryDTO category) {
        //TODO
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setMessage("categories created successfully");
        responseDTO.setData(categoryService.updateCategory(category));
        return responseDTO;
    }

    @DeleteMapping("api/v1/admin/categories/{id}")
    public ResponseDTO deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return new ResponseDTO("category deleted successfully", null);
    }
}

