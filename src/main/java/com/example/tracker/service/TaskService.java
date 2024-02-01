package com.example.tracker.service;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.dto.UserDto;
import com.example.tracker.model.Task;
import com.example.tracker.model.User;
import com.example.tracker.repository.TaskRepository;
import com.example.tracker.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    private final UserRepository userRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    public Flux<TaskDto> getAll() {

        log.info("List os Tasks was sent via controller at" + " time: " + LocalDateTime.now());

        Flux<Task> taskFlux = repository.findAll();
        Flux<User> userFlux = userRepository.findAll();

        return Flux.zip(taskFlux, userFlux, userAssignee()).flatMap(
                tuple -> Flux.just(
                new TaskDto(
                tuple.getT1().getId(),
                tuple.getT1().getName(),
                tuple.getT1().getDescription(),
                tuple.getT1().getCreatedAt(),
                tuple.getT1().getUpdatedAt(),
                tuple.getT1().getStatus(),
                tuple.getT1().getAuthorId(),
                tuple.getT1().getAssigneeId(),
                tuple.getT1().getObserverIds(),
                objectMapper.convertValue(tuple.mapT2(user -> tuple.getT1().getAuthorId()), UserDto.class),
                objectMapper.convertValue(tuple.mapT2(user -> tuple.getT1().getAssigneeId()), UserDto.class),
                new HashSet<>(tuple.getT3()))));
    }

    public Mono<TaskDto> getById(String id) {

        log.info("Task with id: {} was sent via service at" + " time: " + LocalDateTime.now(), id);

        Mono<Task> taskMono = repository.findById(id);
        Mono<User> userMonoAuthor = taskMono.flatMap(user -> userRepository.findById(user.getAuthorId()));
        Mono<User> userMonoAssignee = taskMono.flatMap(user -> userRepository.findById(user.getAssigneeId()));

        List<List<User>> listMono = userAssignee().collectList().share().block();;
        List<User> list = new ArrayList<>();
        for (List<User> i : listMono) {
            for (int j = 0; j < i.size(); j++) {
                list.add(i.get(j));
            }
        }

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
                        new HashSet<>(list)
                ));
    }

    public Mono<Task> create(TaskDto taskDto) {

        log.info("Task was created via service at" + " time: " + LocalDateTime.now());
        taskDto.setCreatedAt(Instant.now());
        Task task = (objectMapper.convertValue(taskDto, Task.class));
        repository.save(task);
        return repository.save(task);
    }

    public Mono<Task> update(String id, String userId, TaskDto taskDto) {

        log.info("Task with id: {} and with userId: {}" +
                " was updated via service at" + " time: " + LocalDateTime.now(), id, userId);

        return getById(id).flatMap(taskForUpdate -> {

            if (StringUtils.hasText(taskDto.getName())) {
                taskForUpdate.setName(taskDto.getName());
            }

            if (StringUtils.hasText(taskDto.getDescription())) {
                taskForUpdate.setDescription(taskDto.getDescription());
            }

            taskForUpdate.setUpdatedAt(Instant.now());

            if (StringUtils.hasText(taskDto.getAssigneeId())) { // TODO: проверить тз
                taskForUpdate.setAssigneeId(taskDto.getAssigneeId());
            }

            if (StringUtils.hasText(taskDto.getAuthorId())) { // TODO: проверить тз
                taskForUpdate.setAuthorId(taskDto.getAuthorId());

            }

            if (taskDto.getStatus() != null) {
                taskForUpdate.setStatus(taskDto.getStatus());
            }

            if (taskDto.getObserverIds() != null) {
                taskForUpdate.setObserverIds(taskDto.getObserverIds());
            }

          return repository.save(objectMapper.convertValue(taskForUpdate, Task.class));
        });
    }

    public Mono<TaskDto> addAssignee(String id, String assigneeId, TaskDto taskDto) {

        log.info("Task with id: {} and with assigneeId: {}" +
                " was updated via controller at" + " time: " + LocalDateTime.now(), id, assigneeId);

        return getById(id).flatMap(taskForUpdate -> {

            if (StringUtils.hasText(taskDto.getAssigneeId())) {
                taskForUpdate.setAssigneeId(taskDto.getAssigneeId());
            }
            repository.save(objectMapper.convertValue(taskForUpdate, Task.class));
            return Mono.just(objectMapper.convertValue(taskForUpdate, TaskDto.class));

        });
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
}
