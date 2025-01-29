package com.dev.fellipe.anime_service.controller;

import com.dev.fellipe.anime_service.domain.Anime;
import com.dev.fellipe.anime_service.mapper.AnimeMapper;
import com.dev.fellipe.anime_service.request.AnimePostRequest;
import com.dev.fellipe.anime_service.response.AnimeGetResponse;
import com.dev.fellipe.anime_service.response.AnimePostResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController()
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.INSTACE;

    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request recived to list all animes, param name '{}'", name);

        var animes = Anime.getAnimes();
        var animeGetResponseList = MAPPER.toAnimeGetResponseList(animes);

        if (name == null) return ResponseEntity.ok(animeGetResponseList);

         var response = animeGetResponseList.stream().filter(anime -> anime.getName().equalsIgnoreCase(name)).toList();

         return ResponseEntity.ok(response);
    }


    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        var animeGetResponse =  Anime.getAnimes()
                .stream().filter(animes -> animes.getId().equals(id))
                .findFirst()
                .map(MAPPER::toAnimeGetResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));

        return ResponseEntity.ok(animeGetResponse);

    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest request) {
        log.debug("Request to save anime: {}", request);

        var anime = MAPPER.toAnime(request);

        Anime.getAnimes().add(anime);

        var response = MAPPER.toAnimePostResponse(anime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);

        var animeToDelete =  Anime.getAnimes()
                .stream().filter(animes -> animes.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Anime not found"));

        Anime.getAnimes().remove(animeToDelete);

        return ResponseEntity.noContent().build();
    }

}
