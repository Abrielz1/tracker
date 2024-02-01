package com.example.tracker.service;

import com.example.tracker.dto.UserDto;
import com.example.tracker.model.User;
import com.example.tracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository repository;

    ObjectMapper objectMapper = new ObjectMapper();

    public Flux<UserDto> getAll() {

        log.info("users were sent to controller via service at" + " time: " + LocalDateTime.now());
        return repository.findAll().map(user ->
                        objectMapper.convertValue(user, UserDto.class));
    }

    public Mono<UserDto> getById(String id) {

        log.info("User with id: {} was sent from db via service to controller at" + " time: " + LocalDateTime.now(), id);
        return repository.findById(id).map(user ->
                        objectMapper.convertValue(user, UserDto.class));
    }

    public Mono<User> create(UserDto userDto) {

        log.info("User with id: {} was created via service at" + " time: " + LocalDateTime.now(), userDto.getId());
        userDto.setId(UUID.randomUUID().toString());
        User user = objectMapper.convertValue(userDto, User.class);
//        repository.save(userDto);

        //return Mono.just(objectMapper.convertValue(user, UserDto.class));

        return repository.save(user);
    }

    public Mono<UserDto> update(String id, UserDto userDto) {

       log.info("User with id: {} was updated via controller at" + " time: " + LocalDateTime.now(), id);
        return getById(id).flatMap(userForUpdate -> {

            if (StringUtils.hasText(userDto.getUsername())) { //not null and not blanc
                userForUpdate.setUsername(userDto.getUsername());
            }

            if (StringUtils.hasText(userDto.getEmail())) { //not null and not blanc
                userForUpdate.setEmail(userDto.getEmail());
            }

            repository.save(objectMapper.convertValue(userForUpdate, User.class));
            return Mono.just(objectMapper.convertValue(userForUpdate, UserDto.class));
        });
    }

    public Mono<Void> removeById(String id) {

        log.info("User with id: {} was removed from db via service at" + " time: " + LocalDateTime.now(), id);
        return repository.deleteById(id);
    }
}
