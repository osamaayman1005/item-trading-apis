package com.baddel.baddelBackend.item.repositories;

import com.baddel.baddelBackend.item.dtos.CategoryDTO;
import com.baddel.baddelBackend.item.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentIsNullOrderByName();
    Optional<Category> findByNameAndParent(String name, Category Parent);
    Optional<Category> findByName(String name);
}
