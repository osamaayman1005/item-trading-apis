package com.baddel.baddelBackend.user.services;

import com.baddel.baddelBackend.exception.ApiException;
import com.baddel.baddelBackend.global.DummyItemLinks;
import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.user.dtos.UserDTO;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        ApplicationUser user = userRepository.findByEmail(identifier)
                .orElseGet(() -> userRepository.findByPhoneNumber(identifier)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));

        return user;
    }
    public ApplicationUser getUserByUsername(String username) {
        return userRepository.findByEmail(username)
                .orElseGet(() -> userRepository.findByPhoneNumber(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }
    public ApplicationUser getUserById(Long id) {
        return userRepository.findById(id)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    public UserDTO updateUser(UserDTO user, String username){
        Long sessionUserId = getUserByUsername(username).getId();
        if (!sessionUserId.equals(user.getId())){
            throw new ApiException("you don't have access to update this user", HttpStatus.BAD_REQUEST);

        }
        ApplicationUser currentUser = userRepository.findById(user.getId())
                .orElseThrow(() -> {
                    throw new ApiException("user not found", HttpStatus.BAD_REQUEST);
                });
        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhoneNumber(user.getPhoneNumber());
        //TODO
        //currentUser.setMedia(user.getMedia());
        currentUser.setMedia(
                user.getMedia()==null?null:
                new Media(DummyItemLinks.getRandomMedia(), ""));

        ApplicationUser updatedUser = userRepository.save(currentUser);
        return new UserDTO(updatedUser.getId(),updatedUser.getFirstName(),updatedUser.getLastName(),
                updatedUser.getEmail(),updatedUser.getPhoneNumber(),updatedUser.getMedia());
    }
}
