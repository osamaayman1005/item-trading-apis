package com.baddel.baddelBackend.user.models;

import com.baddel.baddelBackend.global.models.Media;
import com.baddel.baddelBackend.id_generator.LongIdGenerator;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name= "application_user")
@Setter
@Getter
@AllArgsConstructor
@ToString
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(generator = LongIdGenerator.generatorName)
    @GenericGenerator(name = LongIdGenerator.generatorName,
            strategy = "com.baddel.baddelBackend.id_generator.LongIdGenerator")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    @Column(unique = true, name = "phone_number")
    private String phoneNumber;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_junction",
            joinColumns = {@JoinColumn(name = "user_id"),},
            inverseJoinColumns = {@JoinColumn(name = "role_id"),}
    )
    private Set<Role> authorities;
    @ManyToMany
    @JoinTable(
            name = "community_members_junction")
    private List<Community> communities;
    public ApplicationUser(){
        super();
        this.authorities = new HashSet<>();
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "media_id", referencedColumnName = "id")
    private Media media;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
