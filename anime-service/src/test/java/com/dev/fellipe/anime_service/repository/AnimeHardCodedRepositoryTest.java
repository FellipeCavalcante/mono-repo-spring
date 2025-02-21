package com.dev.fellipe.anime_service.repository;

import com.dev.fellipe.anime_service.commons.AnimeUtils;
import com.dev.fellipe.anime_service.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class AnimeHardCodedRepositoryTest {
    @InjectMocks
    private AnimeHardCodedRepository repository;

    @Mock
    private AnimeData animeData;
    private List<Anime> animeList;

    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll returns a lost with all animes")
    void findAll_ReturnAllAnime_WhenSuccesful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.findAll();
        Assertions.assertThat(animes).hasSize(animes.size());
    }


    @Test
    @DisplayName("findById returns a anime with given id")
    void findById_ReturnAnimeById_WhenSuccesful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var expectedAnime = animeList.getFirst();
        var animes = repository.findById(expectedAnime.getId());
        Assertions.assertThat(animes).isPresent().contains(expectedAnime);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    void findByName_ReturnEmptyList_WhenNameIsNull() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animes = repository.findByName(null);
        Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    void findByName_ReturnsAnimeList_WhenNameExists() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var expectedAnime = animeList.getFirst();
        var animes = repository.findByName(expectedAnime.getName());
        Assertions.assertThat(animes).contains(expectedAnime);
    }

    @Test
    @DisplayName("Save creates a anime")
    void save_CreatesProducer_WhenSuccesful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToSave = animeUtils.newAnimeToSave();

        var anime = repository.save(animeToSave);
        Assertions.assertThat(anime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();

        var animeSavedOptional = repository.findById(animeToSave.getId());
        Assertions.assertThat(animeSavedOptional).isPresent().contains(animeToSave);
    }

    @Test
    @DisplayName("delete remove a anime")
    void delete_RemovesAnime_WhenSuccesful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToRemove = animeList.getFirst();
        repository.delete(animeToRemove);
        Assertions.assertThat(this.animeList).doesNotContain(animeToRemove);
    }

    @Test
    @DisplayName("update uptade a anime")
    void update_RemovesAnime_WhenSuccesful() {
        BDDMockito.when(animeData.getAnimes()).thenReturn(animeList);

        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("test");
        repository.update(animeToUpdate);
        Assertions.assertThat(this.animeList).contains(animeToUpdate);

        var animeUpdatedOptional = repository.findById(animeToUpdate.getId());
        Assertions.assertThat(animeUpdatedOptional).isPresent();
        Assertions.assertThat(animeUpdatedOptional.get().getName()).isEqualTo("test");
    }
}