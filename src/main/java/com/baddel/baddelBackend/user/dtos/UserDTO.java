package com.baddel.baddelBackend.user.dtos;

import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.item.dtos.ItemDTO;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Media media;

    public UserDTO(ApplicationUser user){
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.media = user.getMedia();
    }
}
