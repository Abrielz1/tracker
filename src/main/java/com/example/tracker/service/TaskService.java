package com.example.tracker.service;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.dto.UserDto;
import com.example.tracker.mapper.UserMapper;
import com.example.tracker.model.Task;
import com.example.tracker.model.User;
import com.example.tracker.repository.TaskRepository;
import com.example.tracker.repository.UserRepository;
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
import static com.example.tracker.mapper.TaskMapper.TASK_MAPPER;
import static com.example.tracker.mapper.UserMapper.USER_MAPPER;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    private final UserRepository userRepository;

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
        return Flux.zip(taskFlux, userFlux, userAssignee()).flatMap(tuple -> Flux.just(new TaskDto(
                tuple.getT1().getId(),
                tuple.getT1().getName(),
                tuple.getT1().getDescription(),
                tuple.getT1().getCreatedAt(),
                tuple.getT1().getUpdatedAt(),
                tuple.getT1().getStatus(),
                tuple.getT1().getAuthorId(),
                tuple.getT1().getAssigneeId(),
                tuple.getT1().getObserverIds(),
                tuple.getT2(), //TODO:  (в ответе также должны находиться вложенные сущности, которые описывают автора задачи и исполнителя,
                tuple.getT2(), //TODO:  а также содержат список наблюдающих за задачей) @GetMapping() getAll()
                new HashSet<>((Collection) tuple.getT3()))));
    }

    // TODO: найти конкретную задачу по Id
    //  (в ответе также должны находиться вложенные сущности,
    //  которые описывают автора задачи и исполнителя,
    //  а также содержат список наблюдающих за задачей) @GetMapping("{/id}") getById(@Positive @PathVariable String id)

    public Mono<TaskDto> getById(String id) {

        log.info("Task with id: {} was sent via service at" + " time: " + LocalDateTime.now(), id);
        Mono<Task> taskMono = repository.findById(id);
        Mono<User> userMonoAuthor = userRepository.findById(taskMono.block().getAuthorId());
        Mono<User> userMonoAssignee = userRepository.findById(taskMono.block().getAssigneeId());
       // List<UserDto> observers = (List<User>) userAssignee();

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
                        tuple.getT2(), //TODO:  (в ответе также должны находиться вложенные сущности, которые описывают автора задачи и исполнителя,
                        tuple.getT3(), //TODO:  а также содержат список наблюдающих за задачей) @GetMapping() getAll()
                        new HashSet<>())
        );
    }

    public Mono<TaskDto> create(TaskDto taskDto) {

        log.info("Task was created via service at" + " time: " + LocalDateTime.now());
        Task task = repository.save(TASK_MAPPER.toTask(taskDto)).block();
        return Mono.just(TASK_MAPPER.toTaskDto(task));
    }

    public Mono<TaskDto> update(String id, String userId, TaskDto taskDto) {

        log.info("Task with id: {} and with userId: {}" +
                " was updated via service at" + " time: " + LocalDateTime.now(), id, userId);


        Task task = repository.findById(id).block();
        User user = userRepository.findById(userId).block();

        if (StringUtils.hasText(taskDto.getName())) {
            if (task != null) { // TODO: проверить этот совет idea
                task.setName(taskDto.getName());
            }
        }

        if (StringUtils.hasText(taskDto.getDescription())) {
            if (task != null) {
                task.setDescription(taskDto.getDescription());
            }
        }

        if (taskDto.getCreatedAt() != null && taskDto.getCreatedAt().isBefore(Instant.now())) {
            if (task != null) {
            task.setCreatedAt(taskDto.getCreatedAt());
            }
        }

        if (taskDto.getUpdatedAt() != null && taskDto.getUpdatedAt().isBefore(Instant.now())) {
            if (task != null) {
                task.setUpdatedAt(taskDto.getUpdatedAt());
            }
        }

        if (StringUtils.hasText(taskDto.getAssigneeId())) { // TODO: проверить тз
            if (task != null) {
                task.setAssigneeId(taskDto.getAssigneeId());
            }
        }

        if (StringUtils.hasText(taskDto.getAuthorId())) { // TODO: проверить тз
            if (task != null) {
                task.setAuthorId(taskDto.getAuthorId());
            }
        }

        if (taskDto.getStatus() != null) {
            if (task != null) {
                task.setStatus(taskDto.getStatus());
            }
        }

        task = repository.save(TASK_MAPPER.toTask(taskDto)).block();
        return Mono.just(TASK_MAPPER.toTaskDto(task));
    }

    public Mono<TaskDto> addAssignee(String id, String assigneeId, TaskDto taskDto) {

        Task task = repository.findById(id).block();
        User user = userRepository.findById(assigneeId).block();

        log.info("Task with id: {} and with assigneeId: {}" +
                " was updated via controller at" + " time: " + LocalDateTime.now(), id, assigneeId);

        if (StringUtils.hasText(taskDto.getAssigneeId())) {
            if (task != null) {
                task.setAssigneeId(taskDto.getAssigneeId());
            }
        }

        task = repository.save(TASK_MAPPER.toTask(taskDto)).block();
        return Mono.just(TASK_MAPPER.toTaskDto(task));
    }

    public Mono<Void> removeById(String id) {

        log.info("Task with id: {} was removed via service at" + " time: " + LocalDateTime.now(), id);
        return repository.deleteById(id);
    }

    private Flux<User> userAssignee() {
        Flux<Task> tasks = repository.findAll();
        Flux<User> userAssignee = tasks.flatMap( x -> userRepository.findById( x.getAssigneeId() ) );
        Flux<List<User>> userObObserver = tasks.flatMap( user -> {
            Set<Mono<User>> monoset = new HashSet<>();
            for ( String observId : user.getObserverIds() ) {
                monoset.add( userRepository.findById( observId ) );
            }

            Flux merged = Flux.empty();
            for ( Mono out : monoset ) {
                merged = merged.mergeWith(out);
            }
            return merged.collectList();
        });

        return userAssignee;
    }
}
