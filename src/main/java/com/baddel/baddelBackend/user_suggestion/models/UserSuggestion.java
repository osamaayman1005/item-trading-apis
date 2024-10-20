package com.baddel.baddelBackend.user_suggestion.models;

import com.baddel.baddelBackend.global.models.BaseEntity;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user_suggestion.enums.UserSuggestionStatus;
import com.baddel.baddelBackend.user_suggestion.enums.UserSuggestionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Entity
@Table(name= "user_suggestion")
@Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
public class UserSuggestion extends BaseEntity {
    @NotNull
    private String title;
    private String description;
    @NotNull
    @Column(name = "suggestion_type")
    @Enumerated(EnumType.STRING)
    private UserSuggestionType suggestionType;
    @Column(name = "suggestion_status")
    @Enumerated(EnumType.STRING)
    private UserSuggestionStatus suggestionStatus;
    @ManyToOne(targetEntity = ApplicationUser.class, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private ApplicationUser user;
    private String adminComment;

}
