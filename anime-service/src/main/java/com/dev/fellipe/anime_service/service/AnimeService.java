package com.dev.fellipe.anime_service.service;

import com.dev.fellipe.anime_service.domain.Anime;
import com.dev.fellipe.anime_service.repository.AnimeHardCodedRepository;
import com.dev.fellipe.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AnimeService {

    private final AnimeHardCodedRepository repository;

    public List<Anime> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public Anime findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Anime not found"));
    }

    public Anime save(Anime anime) {
        return repository.save(anime);
    }

    public void delete(Long id) {
        var anime = findByIdOrThrowNotFound(id);
        repository.delete(anime);
    }

    public void update(Anime animeToUpdate) {
        assertAnimeExist(animeToUpdate.getId());
        repository.update(animeToUpdate);
    }

    public void assertAnimeExist(Long id) {
        findByIdOrThrowNotFound(id);
    }
}
