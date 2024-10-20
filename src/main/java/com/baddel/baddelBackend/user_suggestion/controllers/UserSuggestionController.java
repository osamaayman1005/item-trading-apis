package com.baddel.baddelBackend.user_suggestion.controllers;

import com.baddel.baddelBackend.global.dtos.ResponseDTO;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user_suggestion.dtos.UserSuggestionDTO;
import com.baddel.baddelBackend.user_suggestion.services.UserSuggestionService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/suggestions")
public class UserSuggestionController {
    private final UserSuggestionService userSuggestionService;

    public UserSuggestionController(UserSuggestionService userSuggestionService) {
        this.userSuggestionService = userSuggestionService;
    }
    @PostMapping
    public ResponseDTO createSuggestion(@RequestBody UserSuggestionDTO userSuggestionDTO,
                                        Authentication authentication){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(userSuggestionService.saveUserSuggestion(userSuggestionDTO,
                authentication.getName()));
        responseDTO.setMessage("Thank you for your suggestion. our team will review your suggestion now");
        return responseDTO;
    }
}
