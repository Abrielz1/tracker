package com.example.tracker.controller;

import com.example.tracker.Create;
import com.example.tracker.Update;
import com.example.tracker.dto.TaskDto;
import com.example.tracker.model.Task;
import com.example.tracker.publisher.TaskUpdatesPublisher;
import com.example.tracker.service.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Slf4j
@RestController
@Validated
@RequestMapping("/task-tracker/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService service;

    private final TaskUpdatesPublisher publisher;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or ('MANAGER')")
    public Flux<TaskDto> getAll() {

        log.info("List os Tasks was sent via controller at" + " time: " + LocalDateTime.now());
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or ('MANAGER')")
    public Mono<TaskDto> getById(@PathVariable String id) {

        log.info("Task with id: {} was sent via controller at" + " time: " + LocalDateTime.now(), id);
        return service.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<Task> create(@Validated(Create.class) @RequestBody TaskDto taskDto) {

        log.info("Task was created and id: {} was st via controller at" + " time: " + LocalDateTime.now(), taskDto.getId());
        return service.create(taskDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<Task> update(@PathVariable String id,
                             @RequestParam("userId") String userId,
                             @Validated(Update.class) @RequestBody TaskDto taskDto) {

        log.info("Task with id: {} and with userId: {}" +
                " was updated via controller at" + " time: " + LocalDateTime.now(), id, userId);
        return service.update(id, userId, taskDto);
    }

    @PutMapping("/addObserver/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER') or ('MANAGER')")
    public Mono<Task> addObserver(@PathVariable String id,
                                  @RequestParam(name = "observerId") String observerId) {
        log.info("Task with id: {} and with observerId: {}" +
                " was updated via controller at" + " time: " + LocalDateTime.now(), id, observerId);
        return service.addObserver(id, observerId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER')")
    public Mono<Void> removeById(@PathVariable String id) {

        log.info("Task with id: {} was removed via controller at" + " time: " + LocalDateTime.now(), id);
        return service.removeById(id);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("hasRole('MANAGER')")
    public Flux<ServerSentEvent<TaskDto>> getTaskUpdatesStream() {

        log.info("Task were sent via controller by stream");
        return publisher.getUpdateSink()
                .asFlux()
                .map(task -> ServerSentEvent
                        .builder(task)
                        .build());
    }
}
