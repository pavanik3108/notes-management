package com.speer.notes.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.speer.notes.entity.User;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUsername(String username);

}
