package com.example.tracker.controller;

import com.example.tracker.Create;
import com.example.tracker.dto.UserDto;
import com.example.tracker.dto.UserNewDto;
import com.example.tracker.enums.RoleType;
import com.example.tracker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService service;

    private final ObjectMapper objectMapper;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserDto> create(@Validated(Create.class) @RequestBody UserNewDto userDto,
                             @RequestParam(name = "type") RoleType roleType) {

        log.info("User was created via controller at" + " time: " + LocalDateTime.now());
        UserDto userDtoResponse = objectMapper.convertValue(service.create(userDto, roleType), UserDto.class);
        return Mono.just(userDtoResponse);
    }
}
