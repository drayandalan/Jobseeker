package com.ray.api.jobseeker.repository;

import com.mongodb.lang.NonNull;
import com.ray.api.jobseeker.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
