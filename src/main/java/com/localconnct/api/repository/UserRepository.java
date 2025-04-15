package com.localconnct.api.repository;

import com.localconnct.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    User findByEmail(String email);
    void deleteByEmail(String email);

    List<User> findByRoles(String  role);
    Optional<User> findById(String providerId);
}
