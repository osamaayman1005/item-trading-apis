package com.baddel.baddelBackend.global.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "media")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Media extends BaseEntity {
    private String link;
    private String base64;
}
