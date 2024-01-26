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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
//        List<User> userFluxObserver = (List<User>) userRepository.findAll();
//        List<Task> taskList = (List<Task>) repository.findAll();
//        List<User> result = new ArrayList<>();
//        for (int i = 0; i < taskList.size(); i++) {
//           Task t = taskList.get(i);
//          List<String> strings= (List<String>) t.getObserverIds();
//            for (String s : strings) {
//                result.add(userFluxObserver.get(Integer.parseInt(s)));
//            }
//        }
        // Mono<User> observer = userRepository.findById().block();

        List<User> userList = (List<User>) userAssignee();

        List<UserDto> userDtoList = userList.stream().map(user ->
                        objectMapper.convertValue(user, UserDto.class))
                .toList();

        Flux<UserDto> flux = (Flux<UserDto>) userDtoList;
        return Flux.zip(taskFlux, userFlux, flux).flatMap(
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
                new UserDto(), //TODO:  (в ответе также должны находиться вложенные сущности, которые описывают автора задачи и исполнителя,
                new UserDto(), //TODO:  а также содержат список наблюдающих за задачей) @GetMapping() getAll()
                new HashSet<>())));
    }

    // TODO: найти конкретную задачу по Id
    //  (в ответе также должны находиться вложенные сущности,
    //  которые описывают автора задачи и исполнителя,
    //  а также содержат список наблюдающих за задачей) @GetMapping("{/id}") getById(@Positive @PathVariable String id)

    public Mono<TaskDto> getById(String id) {

        log.info("Task with id: {} was sent via service at" + " time: " + LocalDateTime.now(), id);
        Mono<Task> taskMono = repository.findById(id);
   //     Mono<User> userMonoAuthor = userRepository.findById(taskMono.block().getAuthorId());
   //     Mono<User> userMonoAssignee = userRepository.findById(taskMono.block().getAssigneeId());

        List<UserDto> userList = (List<UserDto>) userAssignee();
        List<UserDto> list = userList.stream()
                .map(user ->
                        objectMapper.convertValue(user, UserDto.class))
                .toList();

        return Mono.zip(taskMono, (Mono<?>) list).map(
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
                        new UserDto(), //TODO:  (в ответе также должны находиться вложенные сущности, которые описывают автора задачи и исполнителя,
                        new UserDto(), //TODO:  а также содержат список наблюдающих за задачей) @GetMapping() getAll()
                        new HashSet<>()
                ));
    }

    public Mono<TaskDto> create(TaskDto taskDto) {

        log.info("Task was created via service at" + " time: " + LocalDateTime.now());
        taskDto.setCreatedAt(Instant.now());
        Task task = (objectMapper.convertValue(taskDto, Task.class));
        repository.save(task);
        return Mono.just(objectMapper.convertValue(task, TaskDto.class));
    }

    public Mono<TaskDto> update(String id, String userId, TaskDto taskDto) {

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

            repository.save(objectMapper.convertValue(taskForUpdate, Task.class));
            return Mono.just(objectMapper.convertValue(taskForUpdate, TaskDto.class));
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

    private Flux<User> userAssignee() {
        Flux<Task> tasks = repository.findAll();
        Flux<User> userAssignee = tasks.flatMap(x -> userRepository.findById(x.getAssigneeId()));
        Flux<List<User>> userObObserver = tasks.flatMap(user -> {
            Set<Mono<User>> monoset = new HashSet<>();
            for (String observId : user.getObserverIds()) {
                monoset.add(userRepository.findById(observId));
            }

            Flux merged = Flux.empty();
            for (Mono out : monoset) {
                merged = merged.mergeWith(out);
            }
            return merged.collectList();
        });

        return userAssignee;
    }
}
