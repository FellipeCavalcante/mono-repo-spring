package com.dev.fellipe.anime_service.producer;

import com.dev.fellipe.anime_service.domain.Producer;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends
    JpaRepository<Producer, Long> {

  List<Producer> findByName(
      String name);
}
