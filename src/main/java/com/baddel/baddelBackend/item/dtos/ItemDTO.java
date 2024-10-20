package com.baddel.baddelBackend.item.dtos;

import com.baddel.baddelBackend.item.enums.ItemCondition;
import com.baddel.baddelBackend.item.enums.ItemStatus;
import com.baddel.baddelBackend.item.models.ItemMedia;
import com.baddel.baddelBackend.item.models.PriceVariation;
import lombok.*;

import java.util.List;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
public class ItemDTO {

    private Long id;
    private String name;
    private Double price;
    private String description;
    private ItemCondition condition;
    private ItemStatus status;
    private PriceVariation priceVariation;
    private Long listerId;
    private List<ItemMedia> media;
    private Long categoryId;
}
