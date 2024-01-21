package com.example.tracker.service;

import com.example.tracker.dto.TaskDto;
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
import static com.example.tracker.mapper.TaskMapper.TASK_MAPPER;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    private final UserRepository userRepository;

    // TODO: найти все задачи
    //  (в ответе также должны находиться вложенные сущности, которые описывают автора задачи и исполнителя,
    //  а также содержат список наблюдающих за задачей) @GetMapping() getAll()

    public Flux<TaskDto> getAll() {

        log.info("List os Tasks was sent via controller at" + " time: " + LocalDateTime.now());
        return null;
    }

    // TODO: найти конкретную задачу по Id
    //  (в ответе также должны находиться вложенные сущности,
    //  которые описывают автора задачи и исполнителя,
    //  а также содержат список наблюдающих за задачей) @GetMapping("{/id}") getById(@Positive @PathVariable String id)

    public Mono<TaskDto> getById(String id) {

        log.info("Task with id: {} was sent via service at" + " time: " + LocalDateTime.now(), id);
        return null;
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
}
