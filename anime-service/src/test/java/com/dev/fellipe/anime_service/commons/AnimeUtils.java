package com.dev.fellipe.anime_service.commons;

import com.dev.fellipe.anime_service.domain.Anime;
import com.dev.fellipe.anime_service.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AnimeUtils {

    public List<Anime> newAnimeList() {
        var fullMetal = Anime.builder().id(1L).name("Full Metal Brotherhood").build();
        var steinsGate = Anime.builder().id(2L).name("Steins Gate").build();
        var mashle = Anime.builder().id(3L).name("Mashle").build();

        return new ArrayList<>(List.of(fullMetal, steinsGate, mashle));
    }

    public Anime newAnimeToSave() {
        return Anime.builder()
                .id(99L)
                .name("Overlord")
                .build();
    }
}
