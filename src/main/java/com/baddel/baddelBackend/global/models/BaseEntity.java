package com.baddel.baddelBackend.global.models;

import com.baddel.baddelBackend.id_generator.LongIdGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

@MappedSuperclass @Setter @Getter
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(generator = LongIdGenerator.generatorName)
    @GenericGenerator(name = LongIdGenerator.generatorName,
            strategy = "com.baddel.baddelBackend.id_generator.LongIdGenerator")
    private Long id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}