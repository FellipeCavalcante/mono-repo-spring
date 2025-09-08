package com.dev.fellipe.user_service.service;

import com.dev.fellipe.user_service.domain.Profile;
import com.dev.fellipe.user_service.repository.ProfileRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProfileService {

  private final ProfileRepository repository;

  public List<Profile> findAll() {
    return repository.findAll();
  }


  public Profile save(Profile user) {
    return repository.save(user);
  }

}
