package com.dev.fellipe.user_service.repository;

import com.dev.fellipe.user_service.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends
    JpaRepository<User, Long> {

  List<User> findByFirstNameIgnoreCase(
      String firstName);

  Optional<User> findByEmail(
      String email);

  Optional<User> findByEmailAndIdNot(
      String email, Long id);
}
