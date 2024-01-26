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
import static com.example.tracker.mapper.UserMapper.USER_MAPPER;

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
       // User userDto = repository.findById(id).block(); //не пашет
        return  Mono.just(
                objectMapper.convertValue(repository.findById(id), UserDto.class));
    }

    public Mono<UserDto> create(UserDto userDto) {

        log.info("User with id: {} was created via controller at" + " time: " + LocalDateTime.now(), userDto.getId());
        User user = objectMapper.convertValue(userDto, User.class);
        repository.save(user);
        return Mono.just(objectMapper.convertValue(user, UserDto.class));
    }

    public Mono<UserDto> update(String id, UserDto userDto) {

        log.info("User with id: {} was updated via controller at" + " time: " + LocalDateTime.now(), id);

        User user = repository.findById(id).block();

        if (StringUtils.hasText(userDto.getUsername())) { //not null and not blanc
            user.setUsername((userDto.getUsername()));
        }

        if (StringUtils.hasText(userDto.getEmail())) {
            user.setEmail(userDto.getEmail());
        }

        user = objectMapper.convertValue(userDto, User.class);
        repository.save(user);
        return Mono.just(objectMapper.convertValue(user, UserDto.class));
    }

    public Mono<Void> removeById(String id) {

        log.info("User with id: {} was removed from db via service at" + " time: " + LocalDateTime.now(), id);
        return repository.deleteById(id);
    }
}
