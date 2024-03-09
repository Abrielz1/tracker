package com.example.tracker.controller;

import com.example.tracker.Update;
import com.example.tracker.dto.UserDto;
import com.example.tracker.dto.UserNewDto;
import com.example.tracker.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Slf4j
@Validated
@RestController
@RequestMapping("/task-tracker/users")
@AllArgsConstructor
public class UserController {

    private final UserService service;

    private final ObjectMapper objectMapper;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or ('MANAGER')")
    public Flux<UserDto> getAll() {

        log.info("list of users were sent from controller" + " time: " + LocalDateTime.now());
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or ('MANAGER')")
    public Mono<UserDto> getById(@PathVariable String id) {

        log.info("User was sent from controller at" + " time: " + LocalDateTime.now());
        return service.getById(id);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or ('MANAGER')")
    public Mono<UserDto> update(@PathVariable String id, @Validated(Update.class) @RequestBody UserNewDto userDto) {

        log.info("User with id: {} was updated via controller at" + " time: " + LocalDateTime.now(), id);
        return Mono.just(objectMapper.convertValue(service.update(id, userDto), UserDto.class));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('USER') or ('MANAGER')")
    public Mono<Void> removeById(@PathVariable String id) {

        log.info("user with id: {} was removed via controller at" + " time: " + LocalDateTime.now(), id);
        return service.removeById(id);
    }
}
