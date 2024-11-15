package com.example.tracker.mapper;

import com.example.tracker.dto.UserDto;
import com.example.tracker.dto.UserNewDto;
import com.example.tracker.enums.RoleType;
import com.example.tracker.model.User;

import java.util.ArrayList;

public class UserManualMapper {

    public static User toUser(UserNewDto newUser, RoleType role) {
        return new User(
                null,
                newUser.getUsername(),
                newUser.getEmail(),
                newUser.getPassword(),
                null
        );
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getEmail()
                );
    }
}
