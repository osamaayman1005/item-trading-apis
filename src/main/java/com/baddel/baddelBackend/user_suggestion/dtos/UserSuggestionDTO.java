package com.baddel.baddelBackend.user_suggestion.dtos;

import com.baddel.baddelBackend.user_suggestion.enums.UserSuggestionType;
import com.baddel.baddelBackend.user_suggestion.models.UserSuggestion;
import lombok.*;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
public class UserSuggestionDTO {
    private String title;
    private String description;
    private UserSuggestionType type;

    public UserSuggestionDTO(UserSuggestion userSuggestion){
        this.title = userSuggestion.getTitle();
        this.description = userSuggestion.getDescription();
        this.type = userSuggestion.getSuggestionType();
    }
}
