package com.baddel.baddelBackend.item.dtos;

import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.item.models.Category;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
    private Media media;
    private List<Category> subCategories;
    private Long parentId;

}
