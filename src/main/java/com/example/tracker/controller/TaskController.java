package com.example.tracker.controller;

import com.example.tracker.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task-tracker/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService service;

    // TODO: найти все задачи
    //  (в ответе также должны находиться вложенные сущности, которые описывают автора задачи и исполнителя,
    //  а также содержат список наблюдающих за задачей) @GetMapping() getAll()

    // TODO: найти конкретную задачу по Id
    //  (в ответе также должны находиться вложенные сущности,
    //  которые описывают автора задачи и исполнителя,
    //  а также содержат список наблюдающих за задачей) @GetMapping("{/id}") getById(@Positive @PathVariable String id)

    // TODO:создать задачу @PostMapping() Create.class create(@Validated(Create.class) @RequestBody User user, @Validated(Create.class) @RequestBody  TaskDto taskDto)

    // TODO: обновить информацию о пользователе @PutMapping("{/id}") (@Positive @PathVariable String id, @Validated(Update.class) @RequestBody @RequestBody User user, @Validated(Update.class) @RequestBody TaskDto taskDto)

    // TODO: удалить пользователя по d @DeleteMapping("{/id}") @Positive @PathVariable String id

}
