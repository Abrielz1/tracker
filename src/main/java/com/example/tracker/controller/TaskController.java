package com.example.tracker.controller;

import com.example.tracker.dto.TaskDto;
import com.example.tracker.publisher.TaskUpdatesPublisher;
import com.example.tracker.service.TaskService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/task-tracker/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService service;

    private final TaskUpdatesPublisher publisher;

    // TODO: найти все задачи
    //  (в ответе также должны находиться вложенные сущности, которые описывают автора задачи и исполнителя,
    //  а также содержат список наблюдающих за задачей) @GetMapping() getAll()

    // TODO: найти конкретную задачу по Id
    //  (в ответе также должны находиться вложенные сущности,
    //  которые описывают автора задачи и исполнителя,
    //  а также содержат список наблюдающих за задачей) @GetMapping("{/id}") getById(@Positive @PathVariable String id)

    // TODO:создать задачу @PostMapping() Create.class create(@Validated(Create.class) @RequestBody User user, @Validated(Create.class) @RequestBody  TaskDto taskDto)

    // TODO: обновить информацию о пользователе @PutMapping("{/id}") (@Positive @PathVariable String id, @Validated(Update.class) @RequestBody @RequestBody User user, @Validated(Update.class) @RequestBody TaskDto taskDto)

    // TODO: добавить наблюдателя в задачу;

    @DeleteMapping("{/id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeById(@NotBlank @PathVariable String id) {

        log.info("Task with id: {} was removed via controller at" + " time: " + LocalDateTime.now(), id);
        return service.removeById(id);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TaskDto>> getTaskUpdatesStream() {

        log.info("");
        return publisher.getUpdateSink().asFlux().map(task -> ServerSentEvent.builder(task).build());
    }
}
