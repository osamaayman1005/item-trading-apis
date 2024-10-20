package com.baddel.baddelBackend.user.dtos;

import lombok.*;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor @ToString
public class LoginDTO {
    private String email;
    private String phoneNumber;
    private String password;
}
