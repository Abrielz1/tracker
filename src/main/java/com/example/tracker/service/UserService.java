package com.example.tracker.service;

import com.example.tracker.dto.UserDto;
import com.example.tracker.model.User;
import com.example.tracker.repository.UserRepository;
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

    public Flux<UserDto> getAll() {

        log.info("users were sent to controller via service at" + " time: " + LocalDateTime.now());
        return repository.findAll().map(USER_MAPPER::toUserDto);
    }

    public Mono<Void> removeById(String id) {

        log.info("User with id: {} was removed from db via service at" + " time: " + LocalDateTime.now(), id);
        return repository.deleteById(id);
    }

    public Mono<UserDto> getById(String id) {

        log.info("User with id: {} was sent from db via service to controller at" + " time: " + LocalDateTime.now(), id);
        return repository.findById(id).map(USER_MAPPER::toUserDto);
    }

    public Mono<UserDto> create(UserDto userDto) {

        log.info("User with id: {} was created via controller at" + " time: " + LocalDateTime.now(), userDto.getId());
        User user = repository.save(USER_MAPPER.toUser(userDto)).block();
        return Mono.just(USER_MAPPER.toUserDto(user));
    }

    public Mono<UserDto> update(String id, UserDto userDto) {

        log.info("User with id: {} was updated via controller at" + " time: " + LocalDateTime.now(), id);
//        return getById(id).flatMap(itemForUpdate -> {
//
//            if (StringUtils.hasText(userDto.getUsername())) { //not null and not blanc
//                itemForUpdate.setUsername((userDto.getUsername()));
//            }
//
//            if (StringUtils.hasText(userDto.getEmail())) {
//                itemForUpdate.setEmail(userDto.getEmail());
//            }
//
//            return repository.save(itemForUpdate); //выдаёт ошибку
//        });
        User user = repository.findById(id).block();

        if (StringUtils.hasText(userDto.getUsername())) { //not null and not blanc
            user.setUsername((userDto.getUsername()));
        }

        if (StringUtils.hasText(userDto.getEmail())) {
            user.setEmail(userDto.getEmail());
        }

        user = repository.save(USER_MAPPER.toUser(userDto)).block();
        return Mono.just(USER_MAPPER.toUserDto(user));
    }
}
