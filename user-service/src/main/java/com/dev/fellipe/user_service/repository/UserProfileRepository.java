package com.dev.fellipe.user_service.repository;

import com.dev.fellipe.user_service.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
