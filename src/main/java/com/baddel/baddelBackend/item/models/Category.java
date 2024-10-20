package com.baddel.baddelBackend.item.models;

import com.baddel.baddelBackend.global.models.BaseEntity;
import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.item.dtos.CategoryDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity @Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
public class Category extends BaseEntity {
    @NotBlank @Column(unique = false)
    private String name;
    private String description;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private Media media;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> subCategories;

    public CategoryDTO toCategoryDto(Boolean includeChildren){
        return  new CategoryDTO(this.getId(),this.name, this.description,
                this.media, includeChildren? this.subCategories : null,
                this.parent!=null?this.parent.getId():null);
    }
}
