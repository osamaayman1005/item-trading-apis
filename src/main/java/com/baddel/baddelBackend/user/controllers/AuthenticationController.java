package com.baddel.baddelBackend.user.controllers;

import com.baddel.baddelBackend.user.dtos.LoginDTO;
import com.baddel.baddelBackend.user.dtos.RegistrationDTO;
import com.baddel.baddelBackend.global.dtos.ResponseDTO;
import com.baddel.baddelBackend.user.services.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
@AllArgsConstructor
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseDTO registerUser(@RequestBody RegistrationDTO registrationDTO){
        Map<String, Object> data = authenticationService.register(registrationDTO);
        return new ResponseDTO("user registered successfully",data);
    }
    @PostMapping("/login")
    public ResponseDTO loginUser(@RequestBody LoginDTO loginDTO){
        Map<String, Object> data = authenticationService.loginUser(loginDTO);
        return new ResponseDTO("user logged in Successfully", data);
    }
}
