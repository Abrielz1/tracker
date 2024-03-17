package com.example.tracker.model;

import com.example.tracker.enums.RoleType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document(collection = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id@
    Indexed
    @Column(name = "id")
    private String id;

    @NotBlank
    @Column(unique = true, name = "username")
    private String username;

    @Email
    @NotBlank
    @Column(unique = true, name = "email")
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @Field("roles")
    @EqualsAndHashCode.Exclude
    List<RoleType> roles = new ArrayList<>();

    public void  toAuthority(RoleType roleType) {
        roles.add(roleType);
    }
}
