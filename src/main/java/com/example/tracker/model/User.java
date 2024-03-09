package com.example.tracker.model;

import com.example.tracker.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
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

    @EqualsAndHashCode.Exclude
    private List<Role> role = new ArrayList<>();

    public static UserDto from(User model) {

        UserDto userDto = new UserDto();
        userDto.setUsername(model.getUsername());
        userDto.setEmail(model.getEmail());

        return userDto;
    }
}
