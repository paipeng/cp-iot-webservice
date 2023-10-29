package com.paipeng.iot.repository;

import com.paipeng.iot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    //@Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    User findByEmailAndPassword(String email, String password);
    User findByUsernameAndPassword(String username, String password);
}
