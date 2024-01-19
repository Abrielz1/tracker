package com.example.tracker.repository;

import com.example.tracker.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository  extends ReactiveMongoRepository<User, String> {

}
