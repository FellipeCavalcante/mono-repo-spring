package com.dev.fellipe.user_service.repository;

import com.dev.fellipe.user_service.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("SELECT up FROM UserProfile up join fetch up.user u join fetch up.profile p")
    List<UserProfile> retrieveAll();

//    @EntityGraph(attributePaths = {"user", "profile"})
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();
}
