package com.dev.fellipe.anime_service.controller;

import com.dev.fellipe.anime_service.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController()
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    @GetMapping()
    public List<Anime> listAll(@RequestParam(required = false) String name) {
        var anime = Anime.getAnimes();

        if (name != null) {
            return anime.stream().filter(animes -> animes.getName().equalsIgnoreCase(name)).toList();
        }

        return anime;
    }


    @GetMapping("{id}")
    public Anime findById(@PathVariable Long id) {
        return Anime.getAnimes()
                .stream().filter(animes -> animes.getId().equals(id))
                .findFirst().orElse(null);

    }

    @PostMapping
    public Anime save(@RequestBody Anime anime) {
        anime.setId(ThreadLocalRandom.current().nextLong(100_000));
        Anime.getAnimes().add(anime);
        return anime;
    }
}
