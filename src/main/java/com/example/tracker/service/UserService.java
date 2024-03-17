package com.example.tracker.service;

import com.example.tracker.dto.UserDto;
import com.example.tracker.dto.UserNewDto;
import com.example.tracker.enums.RoleType;
import com.example.tracker.model.User;
import com.example.tracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    private final ObjectMapper objectMapper;

    private final PasswordEncoder encoder;

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

    public Mono<User> create(UserNewDto userDto, RoleType roleType) {

        userDto.setId(UUID.randomUUID().toString());

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(encoder.encode(userDto.getPassword()));
        user.toAuthority(roleType);

        log.info("User with id: {} was created via service at" + " time: " + LocalDateTime.now(), userDto.getId());

        return repository.save(user);
    }

    public Mono<User> update(String id, UserNewDto userDto) {

        log.info("User with id: {} was updated via controller at" + " time: " + LocalDateTime.now(), id);
        return getById(id).flatMap(userForUpdate -> {

            if (StringUtils.hasText(userDto.getUsername())) {
                userForUpdate.setUsername(userDto.getUsername());
            }

            if (StringUtils.hasText(userDto.getEmail())) {
                userForUpdate.setEmail(userDto.getEmail());
            }

            return repository.save(objectMapper.convertValue(userForUpdate, User.class));
        });
    }

    public Mono<Void> removeById(String id) {

        log.info("User with id: {} was removed from db via service at" + " time: " + LocalDateTime.now(), id);
        return repository.deleteById(id);
    }
}
