package com.example.tracker.service;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.dto.UserDto;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.model.Task;
import com.example.tracker.model.User;
import com.example.tracker.repository.TaskRepository;
import com.example.tracker.repository.UserRepository;
import com.example.tracker.security.AppUserPrinciple;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.example.tracker.mapper.UserManualMapper.toUserDto;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    private final UserRepository userRepository;

    public static final User EMPTY = new User();

    ObjectMapper objectMapper;

    public Flux<Task> getAll() {

        log.info("List os Tasks was sent via controller at" + " time: " + LocalDateTime.now());


        Flux<Task> taskFlux = repository.findAll();
        Flux<User> userFlux = userRepository.findAll();

//        return Flux.zip(taskFlux, userFlux, userAssignee()).flatMap(
//                tuple -> Flux.just(
//                        new TaskDto(
//                                tuple.getT1().getId(),
//                                tuple.getT1().getName(),
//                                tuple.getT1().getDescription(),
//                                tuple.getT1().getCreatedAt(),
//                                tuple.getT1().getUpdatedAt(),
//                                tuple.getT1().getStatus(),
//                                tuple.getT1().getAuthorId(),
//                                tuple.getT1().getAssigneeId(),
//                                tuple.getT1().getObserverIds(),
//                                objectMapper.convertValue(tuple.mapT2(user -> tuple.getT1().getAuthorId()), UserDto.class),
//                                objectMapper.convertValue(tuple.mapT2(user -> tuple.getT1().getAssigneeId()), UserDto.class),
//                                new HashSet<>(tuple.getT3()))));

        return mapData(repository.findAll());
    }

    public Mono<TaskDto> getById(String id) {

        log.info("Task with id: {} was sent via service at" + " time: " + LocalDateTime.now(), id);

        Mono<Task> taskMono = repository.findById(id);
        Mono<User> userMonoAuthor = taskMono.flatMap(user -> userRepository.findById(user.getAuthorId()));
        Mono<User> userMonoAssignee = taskMono.flatMap(user -> userRepository.findById(user.getAssigneeId()));
        List<User> userList = Objects.requireNonNull(userAssignee().collectList().share().block())
                .stream().flatMap(List::stream).toList();

        return Mono.zip(taskMono, userMonoAuthor, userMonoAssignee).map(
                tuple -> new TaskDto(
                        tuple.getT1().getId(),
                        tuple.getT1().getName(),
                        tuple.getT1().getDescription(),
                        tuple.getT1().getCreatedAt(),
                        tuple.getT1().getUpdatedAt(),
                        tuple.getT1().getStatus(),
                        tuple.getT1().getAuthorId(),
                        tuple.getT1().getAssigneeId(),
                        tuple.getT1().getObserverIds(),
                        objectMapper.convertValue(tuple.getT2(), UserDto.class),
                        objectMapper.convertValue(tuple.getT3(), UserDto.class),
//                        UserMapper.USER_MAPPER.userToUserDto(tuple.getT2()), // С мап стракт просто дохнет
//                        UserMapper.USER_MAPPER.userToUserDto(tuple.getT3()), // С мап стракт просто дохнет
//                        toUserDto(tuple.getT2()),
//                        toUserDto(tuple.getT3()),
                        new HashSet<>(userList)
                ));
    }

//    public Mono<TaskDto> create(TaskDto taskDto, AppUserPrinciple principle) {
//
//        taskDto.setAuthorId(principle.getId());
//        taskDto.setId(UUID.randomUUID().toString());
//        log.info("Task with id: {} was created via service at" + " time: " + LocalDateTime.now(),
//                taskDto.getId());
//        taskDto.setCreatedAt(Instant.now());
//
//       Mono<Task> taskMono = repository.save(objectMapper.convertValue(taskDto, Task.class));
//
//        return Mono.just(objectMapper.convertValue(taskDto, TaskDto.class));
//    }

    public Mono<Task> create(TaskDto taskDto, AppUserPrinciple principle) {

        taskDto.setAuthorId(principle.getId());
        taskDto.setId(UUID.randomUUID().toString());
        log.info("Task with id: {} was created via service at" + " time: " + LocalDateTime.now(),
                taskDto.getId());
        taskDto.setCreatedAt(Instant.now());

        Mono<Task> taskMono = repository.save(objectMapper.convertValue(taskDto, Task.class));

        return taskMono;
    }

//    public Mono<TaskDto> update(String id, String userId, TaskDto taskDto) {
//
//        log.info("Task with id: {} and with userId: {}" +
//                " was updated via service at" + " time: " + LocalDateTime.now(), id, userId);
//        Mono<Task> task =  getById(id).flatMap(taskForUpdate -> {
//
//            if (StringUtils.hasText(taskDto.getName())) {
//                taskForUpdate.setName(taskDto.getName());
//            }
//
//            if (StringUtils.hasText(taskDto.getDescription())) {
//                taskForUpdate.setDescription(taskDto.getDescription());
//            }
//
//            taskForUpdate.setUpdatedAt(Instant.now());
//
//            if (taskDto.getStatus() != null) {
//                taskForUpdate.setStatus(taskDto.getStatus());
//            }
//
//            if (taskDto.getObserverIds() != null) {
//                taskForUpdate.setObserverIds(taskDto.getObserverIds());
//            }
//
//            return repository.save(objectMapper.convertValue(taskForUpdate, Task.class));
//        });
//
//        return Mono.just(objectMapper.convertValue(task, TaskDto.class));
//    }

    public Mono<Task> update(String id, String userId, TaskDto taskDto) {

        log.info("Task with id: {} and with userId: {}" +
                " was updated via service at" + " time: " + LocalDateTime.now(), id, userId);
        Mono<Task> task =  getById(id).flatMap(taskForUpdate -> {

            if (StringUtils.hasText(taskDto.getName())) {
                taskForUpdate.setName(taskDto.getName());
            }

            if (StringUtils.hasText(taskDto.getDescription())) {
                taskForUpdate.setDescription(taskDto.getDescription());
            }

            taskForUpdate.setUpdatedAt(Instant.now());

            if (taskDto.getStatus() != null) {
                taskForUpdate.setStatus(taskDto.getStatus());
            }

            if (taskDto.getObserverIds() != null) {
                taskForUpdate.setObserverIds(taskDto.getObserverIds());
            }

            return repository.save(objectMapper.convertValue(taskForUpdate, Task.class));
        });

        return task;
    }

    public Mono<TaskDto> addObserver(String id, String observerId) {

        log.info("Task with id: {} and with assigneeId: {}" +
                " was updated via controller at" + " time: " + LocalDateTime.now(), id, observerId);
        Mono<User> userMono = userRepository.findById(observerId);

        Mono<Task> task = getById(id).flatMap(taskForUpdate -> {
            if (StringUtils.hasText(observerId) && taskForUpdate.getObserverIds().size() == 0) {
                Set<User> set = new HashSet<>();
                set.add(objectMapper.convertValue(userMono, User.class));
                taskForUpdate.setObservers(set);
                taskForUpdate.setUpdatedAt(Instant.now());
            } else {
                taskForUpdate.getObservers().add(userMono.block());
                taskForUpdate.setUpdatedAt(Instant.now());
            }

            return repository.save(objectMapper.convertValue(taskForUpdate, Task.class));
        });

        return Mono.just(objectMapper.convertValue(task, TaskDto.class));
    }

    public Mono<Void> removeById(String id) {

        log.info("Task with id: {} was removed via service at" + " time: " + LocalDateTime.now(), id);
        return repository.deleteById(id);
    }

    private Flux<List<User>> userAssignee() {

        Flux<Task> tasks = repository.findAll();

        Flux<List<User>> userObObserver = tasks.flatMap(user -> {
            Set<Mono<User>> monoset = new HashSet<>();

            for (String observerId : user.getObserverIds()) {
                monoset.add(userRepository.findById(observerId));
            }

            Flux merged = Flux.empty();

            for (Mono out : monoset) {
                merged = merged.mergeWith(out);
            }

            return merged.collectList();
        });

        return userObObserver;
    }

    private Flux<Task> mapData(Flux<Task> tasks) {
        return tasks.flatMap(this::ripData);
    }

    private Mono<Task> ripData(Task task) {
        Mono<User> authorMono = task.getAuthorId() != null ? userRepository.findById(task.getAuthorId()) : Mono.empty();
        Mono<User> assigneeMono = task.getAssigneeId() != null ? userRepository.findById(task.getAssigneeId()) : Mono.empty();
        Flux<User> observersFlux = Flux.fromIterable(task.getObserverIds())
                .flatMap(userRepository::findById);

        return Mono.zip(authorMono.defaultIfEmpty(EMPTY), assigneeMono.defaultIfEmpty(EMPTY),
                        observersFlux.collectList())
                .map(t -> {
                    User author = t.getT1();
                    User assignee = t.getT2();
                    List<User> observers = t.getT3();

                    if (author != EMPTY) {
                        task.setAuthor(author);
                    }
                    if (assignee != EMPTY) {
                        task.setAssignee(assignee);
                    }
                    task.setObservers((Set<User>) observers);

                    return task;
                });
    }

}
