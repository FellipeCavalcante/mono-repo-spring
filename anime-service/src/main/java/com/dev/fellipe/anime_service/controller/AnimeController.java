package com.dev.fellipe.anime_service.controller;

import com.dev.fellipe.anime_service.domain.Anime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    @GetMapping()
    public List<Anime> listAll(@RequestParam(required = false) String name) {
        var anime = Anime.getAnime();

        if (name != null) {
            return anime.stream().filter(animes -> animes.getName().equalsIgnoreCase(name)).toList();
        }

        return anime;
    }


    @GetMapping("{id}")
    public Anime findById(@PathVariable Long id) {
        return Anime.getAnime()
                .stream().filter(animes -> animes.getId().equals(id))
                .findFirst().orElse(null);

    }


}
