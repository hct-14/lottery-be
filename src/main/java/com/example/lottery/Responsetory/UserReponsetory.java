package com.example.lottery.Responsetory;

import com.example.lottery.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserReponsetory extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByEmail(String email);
    User findByEmail(String email);

    User findByRefreshTokenAndEmail(String token, String email);

}
