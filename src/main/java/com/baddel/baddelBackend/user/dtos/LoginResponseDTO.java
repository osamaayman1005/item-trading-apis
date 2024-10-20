package com.baddel.baddelBackend.user.dtos;

import com.baddel.baddelBackend.user.models.ApplicationUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor @Setter @Getter
public class LoginResponseDTO {
    private ApplicationUser user;
    private String jwt;
}
