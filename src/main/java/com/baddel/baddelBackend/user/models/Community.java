package com.baddel.baddelBackend.user.models;

import com.baddel.baddelBackend.global.models.BaseEntity;
import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.user.dtos.CommunityDTO;
import com.baddel.baddelBackend.user.enums.CommunityType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
@Entity @Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Community extends BaseEntity {
    private String name;
    private String location;
    private String phoneNumber;
    @NotNull
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CommunityType type;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private Media media;
    @NotNull
    private Boolean isActive;

    @ManyToMany
    @JoinTable(
            name = "community_members_junction",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<ApplicationUser> members;

    public CommunityDTO toCommunityDTO(){
        return new CommunityDTO(
                this.getId(),
                this.name,
                this.location,
                this.phoneNumber,
                this.type,
                this.media,
                this.isActive
        );
    }
}
