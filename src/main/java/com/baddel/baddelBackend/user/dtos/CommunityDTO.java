package com.baddel.baddelBackend.user.dtos;

import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.user.enums.CommunityType;
import com.baddel.baddelBackend.user.models.ApplicationUser;
import com.baddel.baddelBackend.user.models.Community;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommunityDTO {
    private Long id;
    private String name;
    private String location;
    private String phoneNumber;
    private CommunityType type;
    private Media media;
    private Boolean isActive;

    public Community toCommunity(){
        return new Community(this.name,
                this.location,
                this.phoneNumber,
                this.type,this.media,this.isActive,null);
    }
    public Community toCommunity(List<ApplicationUser> members){
        return new Community(this.name,
                this.location,
                this.phoneNumber,
                this.type,this.media,this.isActive,members);
    }

}
