package com.diskagua.api.repository;

import com.diskagua.api.models.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsUserByEmail(String email);

    Optional<User> findUserByEmail(String email);

    void deleteUserByEmail(String email);
}
