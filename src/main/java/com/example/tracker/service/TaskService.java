package com.example.tracker.service;

import com.example.tracker.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public Mono<Void> removeById(String id) {

        log.info("Task with id: {} was removed via service at" + " time: " + LocalDateTime.now(), id);
        return repository.deleteById(id);
    }
}
