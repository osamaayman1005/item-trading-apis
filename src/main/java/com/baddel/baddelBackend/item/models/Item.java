package com.baddel.baddelBackend.item.models;

import com.baddel.baddelBackend.global.models.BaseEntity;
import com.baddel.baddelBackend.item.dtos.ItemDTO;
import com.baddel.baddelBackend.item.enums.ItemCondition;
import com.baddel.baddelBackend.item.enums.ItemStatus;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Table(name= "item")
@AllArgsConstructor @NoArgsConstructor @Setter @Getter @ToString
public class Item extends BaseEntity {
    @NotBlank
    private String name;
    @NotNull
    private Double price;
    private String description;
    @NotNull
    @Column(name = "item_condition")
    @Enumerated(EnumType.STRING)
    private ItemCondition condition;
    @NotNull
    @Column(name = "item_status")
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    private Double maximumAmountToPay;
    private Double maximumAmountToReceive;
    private Boolean isDeleted ;

    @ManyToOne(targetEntity = ApplicationUser.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lister_id", nullable = false)
    private ApplicationUser lister;
    @OneToMany(mappedBy = "item", cascade = CascadeType.PERSIST)
    private List<ItemMedia> itemMedia;

    @ManyToOne(targetEntity = Category.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public ItemDTO toItemDTO() {
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(this.getId());
        itemDTO.setName(this.name);
        itemDTO.setPrice(this.price);
        itemDTO.setDescription(this.description);
        itemDTO.setCondition(ItemCondition.valueOf(this.condition.name()));
        itemDTO.setStatus(ItemStatus.valueOf(this.status.name()));
        itemDTO.setPriceVariation(new PriceVariation(maximumAmountToPay,
                maximumAmountToReceive));
        itemDTO.setListerId(this.lister.getId());
        itemDTO.setCategoryId(this.category.toCategoryDto(false).getId());
        if (itemMedia != null && !itemMedia.isEmpty()) {
            itemDTO.setMedia(itemMedia);
        }
        return itemDTO;
    }

}
