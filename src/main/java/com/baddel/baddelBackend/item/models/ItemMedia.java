package com.baddel.baddelBackend.item.models;

import com.baddel.baddelBackend.global.models.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "item_media")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
public class ItemMedia extends BaseEntity {
    private String link;
    private String base64;
    @JsonIgnore
    @ManyToOne(targetEntity = Item.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
}
