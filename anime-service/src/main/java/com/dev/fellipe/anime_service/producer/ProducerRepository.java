package com.dev.fellipe.anime_service.producer;

import com.dev.fellipe.anime_service.domain.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProducerRepository extends JpaRepository<Producer, Long> {

    List<Producer> findByName(String name);
}
