package com.dev.fellipe.anime_service.controller;

import com.dev.fellipe.anime_service.mapper.AnimeMapper;
import com.dev.fellipe.anime_service.request.AnimePostRequest;
import com.dev.fellipe.anime_service.request.AnimePutRequest;
import com.dev.fellipe.anime_service.response.AnimeGetResponse;
import com.dev.fellipe.anime_service.response.AnimePostResponse;
import com.dev.fellipe.anime_service.service.AnimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("v1/animes")
@Slf4j
public class AnimeController {

    private static final AnimeMapper MAPPER = AnimeMapper.INSTACE;
    private AnimeService service;

    public AnimeController() {
        this.service = new AnimeService();
    }

    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request recived to list all animes, param name '{}'", name);

        var animes = service.findAll(name);
        var animeGetResponseList = MAPPER.toAnimeGetResponseList(animes);

         return ResponseEntity.ok(animeGetResponseList);
    }


    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        var animes = service.findByIdOrThrowNotFound(id);
        var animeGetResponse = MAPPER.toAnimeGetResponse(animes);

        return ResponseEntity.ok(animeGetResponse);

    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> save(@RequestBody AnimePostRequest request) {
        log.debug("Request to save anime : {}", request);
        var anime = MAPPER.toAnime(request);

        var animeSaved = service.save(anime);

        var animeGetResponse = MAPPER.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animeGetResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody AnimePutRequest request) {
        log.debug("Request to update anime {}", request);

        var animeUpdated =  MAPPER.toAnime(request);
        service.update(animeUpdated);

        return ResponseEntity.noContent().build();
    }

}
