package com.mr1ganka.expenseSharing.repository;

import com.mr1ganka.expenseSharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByEmail(String email);
}
