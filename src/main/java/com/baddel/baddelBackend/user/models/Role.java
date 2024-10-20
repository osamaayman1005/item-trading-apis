package com.baddel.baddelBackend.user.models;


import com.baddel.baddelBackend.id_generator.LongIdGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name= "role")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Role  implements GrantedAuthority {

    @Id
    @GeneratedValue(generator = LongIdGenerator.generatorName)
    @GenericGenerator(name = LongIdGenerator.generatorName,
            strategy = "com.baddel.baddelBackend.id_generator.LongIdGenerator")
    private Long id;
    private String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }

}
