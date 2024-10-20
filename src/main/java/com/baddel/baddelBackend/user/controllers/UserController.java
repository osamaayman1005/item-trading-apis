package com.baddel.baddelBackend.user.controllers;

import com.baddel.baddelBackend.global.dtos.ResponseDTO;
import com.baddel.baddelBackend.user.dtos.UserDTO;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/profile")
@CrossOrigin("*")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseDTO getCurrentUserDetails(Authentication authentication) {
        String username = authentication.getName();
        ApplicationUser user = userService.getUserByUsername(username);

        return new ResponseDTO("user data retrieved successfully",
                new UserDTO(user.getId(),user.getFirstName(), user.getLastName(),
                        user.getEmail(),user.getPhoneNumber(), user.getMedia()));

    }

    @PutMapping
    public ResponseDTO updateCurrentUser(Authentication authentication, @RequestBody UserDTO user) {
        String username = authentication.getName();
        return new ResponseDTO("user data retrieved successfully",
               userService.updateUser(user,username));

    }
}
