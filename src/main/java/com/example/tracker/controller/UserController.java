package com.example.tracker.controller;

import com.example.tracker.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/") // TODO: придумать
@AllArgsConstructor
public class UserController {

    private final UserService service;

    // TODO: найти всех пользователей @GetMapping() getAll()

    // TODO: найти пользователя по Id @GetMapping("{/id}") getById(@PathVariable String id)

    // TODO: создать пользователя @PostMapping() Create.class

    // TODO: обновить информацию о пользователе @PutMapping("{/id}") @PathVariable Update.class @RequestBody

    // TODO: удалить пользователя по d @DeleteMapping("{/id}")
}
