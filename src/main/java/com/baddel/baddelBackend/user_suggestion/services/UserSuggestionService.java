package com.baddel.baddelBackend.user_suggestion.services;

import com.baddel.baddelBackend.user.services.UserService;
import com.baddel.baddelBackend.user_suggestion.dtos.UserSuggestionDTO;
import com.baddel.baddelBackend.user_suggestion.enums.UserSuggestionStatus;
import com.baddel.baddelBackend.user_suggestion.models.UserSuggestion;
import com.baddel.baddelBackend.user_suggestion.repositories.UserSuggestionRepository;
import org.springframework.stereotype.Service;

@Service
public class UserSuggestionService {
    private final UserSuggestionRepository userSuggestionRepository;
    private final UserService userService;

    public UserSuggestionService(UserSuggestionRepository userSuggestionRepository, UserService userService) {
        this.userSuggestionRepository = userSuggestionRepository;
        this.userService = userService;
    }
    public UserSuggestionDTO saveUserSuggestion(UserSuggestionDTO userSuggestionDTO, String username){
        UserSuggestion userSuggestion = new UserSuggestion();
        userSuggestion.setTitle(userSuggestionDTO.getTitle());
        userSuggestion.setDescription(userSuggestionDTO.getDescription());
        userSuggestion.setSuggestionType(userSuggestionDTO.getType());
        userSuggestion.setSuggestionStatus(UserSuggestionStatus.RECEIVED);
        userSuggestion.setUser(userService.getUserByUsername(username));
        UserSuggestion savedUserSuggestion = userSuggestionRepository.save(userSuggestion);
        return new UserSuggestionDTO(savedUserSuggestion);
    }
}
