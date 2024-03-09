package com.example.tracker.model;

import com.example.tracker.enums.RoleType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Builder
@Document(collection = "authorities")
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    private String id;

    @Enumerated(value = EnumType.STRING)
    private RoleType authority;


    public GrantedAuthority toAuthority() {

        return new SimpleGrantedAuthority(authority.name());
    }

    public static Role from(RoleType type) {

        var role = new Role();
        role.setAuthority(type);

        return role;
    }
}
