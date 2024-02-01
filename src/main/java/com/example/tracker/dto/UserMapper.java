package com.example.tracker.dto;

import com.example.tracker.model.User;

public class UserMapper {

    public static UserDto fromUser(User model) {

        UserDto userDto = new UserDto();
        userDto.setUsername(model.getUsername());
        userDto.setEmail(model.getEmail());

        return userDto;
    }
}
