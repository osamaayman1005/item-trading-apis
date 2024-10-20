package com.baddel.baddelBackend.user.services;

import com.baddel.baddelBackend.exception.ApiException;
import com.baddel.baddelBackend.security.services.TokenService;
import com.baddel.baddelBackend.user.dtos.LoginDTO;
import com.baddel.baddelBackend.user.dtos.RegistrationDTO;
import com.baddel.baddelBackend.user.dtos.UserDTO;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user.models.Role;
import com.baddel.baddelBackend.user.repositories.RoleRepository;
import com.baddel.baddelBackend.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public Map<String, Object> register(RegistrationDTO user){
        Map<String, Object> data = new HashMap<>();
        user.setPhoneNumber(sanitizePhoneNumber(user.getPhoneNumber()));
        validateUniqueEmailAndPhoneNumber(user.getEmail(), user.getPhoneNumber());

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        Role userRole = roleRepository.findByAuthority("USER").get();

        Set<Role> authorities = new HashSet<>();

        authorities.add(userRole);

        ApplicationUser savedUser = userRepository.save(new ApplicationUser(null,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhoneNumber(),
                encodedPassword,
                authorities,null,null));
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(savedUser.getEmail(), user.getPassword())
            );
            String token = tokenService.generateJwt(authentication);
            UserDTO userDto = new UserDTO(savedUser.getId(),savedUser.getFirstName(), savedUser.getLastName(),
                    savedUser.getEmail(),savedUser.getPhoneNumber(), null);
            data.put("user", userDto);
            data.put("token", token);
            return data;
        } catch (AuthenticationException e) {
            throw new ApiException("Something went wrong \n" + e.getMessage(), HttpStatus.valueOf(500));
        }
    }

    public Map<String, Object> loginUser(LoginDTO loginDTO) {
        Map<String, Object> data = new HashMap<>();
        loginDTO.setPhoneNumber(sanitizePhoneNumber(loginDTO.getPhoneNumber()));
        ApplicationUser user = userRepository.findByEmail(loginDTO.getEmail())
                .orElseGet(() -> userRepository.findByPhoneNumber(loginDTO.getPhoneNumber())
                        .orElseThrow(() ->new ApiException("Wrong credentials",
                                HttpStatus.EXPECTATION_FAILED)));
        System.out.println("user logged in " + user.toString());
        if (user != null) {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(user.getEmail(), loginDTO.getPassword())
                );
                String token = tokenService.generateJwt(authentication);
                UserDTO userDto = new UserDTO(user.getId(),user.getFirstName(), user.getLastName(),
                        user.getEmail(),user.getPhoneNumber(),user.getMedia());
                data.put("user", userDto);
                data.put("token", token);
                return data;
            }catch (BadCredentialsException e){
                throw new ApiException("Wrong credentials",
                        HttpStatus.EXPECTATION_FAILED);
            }
            catch (AuthenticationException e) {
                throw new ApiException("USER NOT FOUND", HttpStatus.NOT_FOUND); // Handle case when user is not found
            }
        } else {
            throw new ApiException("USER NOT FOUND", HttpStatus.NOT_FOUND); // Handle case when user is not found
        }
    }
    private void validateUniqueEmailAndPhoneNumber(String email, String phoneNumber) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ApiException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        if (userRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new ApiException("Phone number already exists", HttpStatus.BAD_REQUEST);
        }
    }
    private String sanitizePhoneNumber(String phoneNumber){
        // Check if the phone number is not null and not empty
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Check if the first character is '0'
            if (phoneNumber.charAt(0) == '0') {
                // Remove the first character
                phoneNumber = phoneNumber.substring(1);
            }
        }
        // Return the sanitized phone number
        return phoneNumber;
    }
}
