package com.example.tracker.repository;

import com.example.tracker.model.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository  extends ReactiveMongoRepository<Task, String> {

}
