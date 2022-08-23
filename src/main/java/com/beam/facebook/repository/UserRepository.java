package com.beam.facebook.repository;

import com.beam.facebook.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    public Optional<User> findByEmail(String email);
}
