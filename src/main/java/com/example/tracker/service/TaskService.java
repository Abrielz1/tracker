package com.example.tracker.service;

import com.example.tracker.repository.TaskRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository repository;
}
