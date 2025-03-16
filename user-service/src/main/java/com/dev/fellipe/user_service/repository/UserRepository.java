package com.dev.fellipe.user_service.repository;

import com.dev.fellipe.user_service.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
