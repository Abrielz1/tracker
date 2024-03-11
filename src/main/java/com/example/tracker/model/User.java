package com.example.tracker.model;

import com.example.tracker.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    private String username;

    private String email;

    private String password;

    private RoleType authority;

    @Field("roles")
    @EqualsAndHashCode.Exclude
    List<RoleType> roles = new ArrayList<>();

    public GrantedAuthority toAuthority() {

        return new SimpleGrantedAuthority(authority.name());
    }
}
