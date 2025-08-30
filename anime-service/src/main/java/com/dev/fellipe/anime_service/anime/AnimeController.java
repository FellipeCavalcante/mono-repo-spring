package com.dev.fellipe.anime_service.anime;

import com.dev.fellipe.anime_service.anime.request.AnimePostRequest;
import com.dev.fellipe.anime_service.anime.request.AnimePutRequest;
import com.dev.fellipe.anime_service.domain.Anime;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("v1/animes")
@Slf4j
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class AnimeController {

    private final AnimeMapper mapper;
    private final AnimeService service;

    @GetMapping()
    public ResponseEntity<List<AnimeGetResponse>> findAllAnimes(@RequestParam(required = false) String name) {
        log.debug("Request recived to list all animes, param name '{}'", name);

        var animes = service.findAll(name);
        var animeGetResponseList = mapper.toAnimeGetResponseList(animes);

        return ResponseEntity.ok(animeGetResponseList);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<AnimeGetResponse>> findAllAnimesPaginated(@ParameterObject Pageable pageable) {
        log.debug("Request recived to list all animes, param paginated");

        var animes = service.findAllPaginated(pageable).map(mapper::toAnimeGetResponse);


        return ResponseEntity.ok(animes);
    }


    @GetMapping("{id}")
    public ResponseEntity<AnimeGetResponse> findByAnimeId(@PathVariable Long id) {
        log.debug("Request to find anime by id: {}", id);

        var animes = service.findByIdOrThrowNotFound(id);
        var animeGetResponse = mapper.toAnimeGetResponse(animes);

        return ResponseEntity.ok(animeGetResponse);

    }

    @PostMapping
    public ResponseEntity<AnimePostResponse> saveAnime(@RequestBody @Valid AnimePostRequest request) {
        log.debug("Request to save anime : {}", request);
        var anime = mapper.toAnime(request);

        var animeSaved = service.save(anime);

        var animeGetResponse = mapper.toAnimePostResponse(animeSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(animeGetResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteByAnimeId(@PathVariable Long id) {
        log.debug("Request to delete anime by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> updateAnime(@RequestBody @Valid AnimePutRequest request) {
        log.debug("Request to update anime {}", request);

        var animeUpdated = mapper.toAnime(request);
        service.update(animeUpdated);

        return ResponseEntity.noContent().build();
    }
}
