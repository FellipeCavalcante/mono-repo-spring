package com.dev.fellipe.anime_service.anime;

import com.dev.fellipe.anime_service.domain.Anime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimeRepository extends
    JpaRepository<Anime, Long> {

  List<Anime> findByName(String name);
}
