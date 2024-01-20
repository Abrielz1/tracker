package com.example.tracker.service;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import static com.example.tracker.mapper.TaskMapper.TASK_MAPPER;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;

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
        return repository.save(TASK_MAPPER.toTask(taskDto)).map(TASK_MAPPER::toTaskDto);
    }

    public Mono<TaskDto> update(String id, String userId, TaskDto taskDto) {

        log.info("Task with id: {} and with userId: {}" +
                " was updated via service at" + " time: " + LocalDateTime.now(), id, userId);
        return null; //todo: дописать логику обновления
    }

    public Mono<Void> removeById(String id) {

        log.info("Task with id: {} was removed via service at" + " time: " + LocalDateTime.now(), id);
        return repository.deleteById(id);
    }
}
