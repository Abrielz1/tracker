package com.example.tracker.repository;

import com.example.tracker.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository  extends ReactiveMongoRepository<User, String> {

    Optional<User> findByUsername(String username);
}
