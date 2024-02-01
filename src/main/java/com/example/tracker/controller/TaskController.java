package com.example.tracker.controller;

import com.example.tracker.Create;
import com.example.tracker.Update;
import com.example.tracker.dto.TaskDto;
import com.example.tracker.model.Task;
import com.example.tracker.publisher.TaskUpdatesPublisher;
import com.example.tracker.service.TaskService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
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
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/task-tracker/tasks") // /task-tracker/users
@AllArgsConstructor
public class TaskController {

    private final TaskService service;

    private final TaskUpdatesPublisher publisher;

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public Flux<TaskDto> getAll() {

        log.info("List os Tasks was sent via controller at" + " time: " + LocalDateTime.now());
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<TaskDto> getById(@PathVariable String id) {

        log.info("Task with id: {} was sent via controller at" + " time: " + LocalDateTime.now(), id);
        return service.getById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Task> create(@Validated(Create.class) @RequestBody TaskDto taskDto) {

        taskDto.setId(UUID.randomUUID().toString());
        log.info("Task was created and id: {} was st via controller at" + " time: " + LocalDateTime.now(), taskDto.getId());
        return service.create(taskDto);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Task> update(@PathVariable String id,
                             @RequestParam("userId") String userId,
                             @Validated(Update.class) @RequestBody TaskDto taskDto) {

        log.info("Task with id: {} and with userId: {}" +
                " was updated via controller at" + " time: " + LocalDateTime.now(), id, userId);
        return service.update(id, userId, taskDto);
    }

    @PutMapping("/addAssignee/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Task> addAssignee(@PathVariable String id,
                                     @RequestParam String assigneeId) {
        log.info("Task with id: {} and with assigneeId: {}" +
                " was updated via controller at" + " time: " + LocalDateTime.now(), id, assigneeId);
        return service.addAssignee(id, assigneeId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> removeById(@PathVariable String id) {

        log.info("Task with id: {} was removed via controller at" + " time: " + LocalDateTime.now(), id);
        return service.removeById(id);
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<TaskDto>> getTaskUpdatesStream() {

        log.info("Task were sent via controller by stream");
        return publisher.getUpdateSink()
                .asFlux()
                .map(task -> ServerSentEvent
                .builder(task)
                .build());
    }
}
