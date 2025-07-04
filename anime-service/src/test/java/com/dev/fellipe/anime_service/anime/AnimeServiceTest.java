package com.dev.fellipe.anime_service.anime;

import com.dev.fellipe.anime_service.commons.AnimeUtils;
import com.dev.fellipe.anime_service.domain.Anime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService service;

    @Mock
    private AnimeRepository repository;
    private List<Anime> animeList;

    @InjectMocks
    private AnimeUtils animeUtils;

    @BeforeEach
    void init() {
        animeList = animeUtils.newAnimeList();
    }

    @Test
    @DisplayName("findAll returns a lost with all animes when argument is null")
    @Order(1)
    void findAll_ReturnAllAnimes_WhenArgumentIsNull() {
        BDDMockito.when(repository.findAll()).thenReturn(animeList);

        var animes = service.findAll(null);

        Assertions.assertThat(animes).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findAllPaginated returns a paginated list of animes")
    @Order(1)
    void findAllPaginated_ReturnAllAnimes_WhenSuccessful() {
        var pageRequest = PageRequest.of(0, animeList.size());
        var pageAnime = new PageImpl<>(animeList, pageRequest, animeList.size());

        BDDMockito.when(repository.findAll(BDDMockito.any(Pageable.class))).thenReturn(pageAnime);

        var animesFound = service.findAllPaginated(pageRequest);

        Assertions.assertThat(animesFound).isNotNull().hasSameElementsAs(animeList);
    }

    @Test
    @DisplayName("findAll returns list with found object when name exists")
    @Order(2)
    void findByName_ReturnsAnimeList_WhenNameExists() {
        Anime anime = animeList.getFirst();
        var expectedAnimesFound = singletonList(anime);
        BDDMockito.when(repository.findByName(anime.getName())).thenReturn(expectedAnimesFound);

        var animesFound = service.findAll(anime.getName());
        org.assertj.core.api.Assertions.assertThat(animesFound).containsAll(expectedAnimesFound);
    }

    @Test
    @DisplayName("findByName returns empty list when name is not found")
    @Order(3)
    void findByName_ReturnEmptyList_WhenNameIsNotFound() {
        var name = "not-found";
        BDDMockito.when(repository.findByName(name)).thenReturn(emptyList());

        var animes = service.findAll(name);
        org.assertj.core.api.Assertions.assertThat(animes).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findById returns a animes with given id")
    @Order(4)
    void findById_ReturnAnimesById_WhenSuccesful() {
        var expectedAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.of(expectedAnime));

        var animes = service.findByIdOrThrowNotFound(expectedAnime.getId());
        org.assertj.core.api.Assertions.assertThat(animes).isEqualTo(expectedAnime);
    }

    @Test
    @DisplayName("findById throws ResponseStatusException when anime is not found")
    @Order(5)
    void findById_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var expectedAnime = animeList.getFirst();
        BDDMockito.when(repository.findById(expectedAnime.getId())).thenReturn(Optional.empty());


        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.findByIdOrThrowNotFound(expectedAnime.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("Save creates a anime")
    @Order(6)
    void save_CreatesAnime_WhenSuccesful() {
        var animeToSave = animeUtils.newAnimeToSave();
        BDDMockito.when(repository.save(animeToSave)).thenReturn(animeToSave);

        var savedAnime = service.save(animeToSave);
        org.assertj.core.api.Assertions.assertThat(savedAnime).isEqualTo(animeToSave).hasNoNullFieldsOrProperties();
    }

    @Test
    @DisplayName("delete remove a anime")
    @Order(7)
    void delete_RemoveAnime_WhenSuccesful() {
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.of(animeToDelete));

        BDDMockito.doNothing().when(repository).delete(animeToDelete);
        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.delete(animeToDelete.getId()));
    }

    @Test
    @DisplayName("delete throws ResponseStatusException when anime is not found")
    @Order(8)
    void delete_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToDelete = animeList.getFirst();
        BDDMockito.when(repository.findById(animeToDelete.getId())).thenReturn(Optional.empty());

        org.assertj.core.api.Assertions.assertThatException()
                .isThrownBy(() -> service.delete(animeToDelete.getId()))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @DisplayName("update updates a anime")
    @Order(9)
    void update_UpdatesAnime_WhenSuccesful() {
        var animeToUpdate = animeList.getFirst();
        animeToUpdate.setName("updatedName");

        BDDMockito.when(repository.findById(animeToUpdate.getId())).thenReturn(Optional.of(animeToUpdate));
        BDDMockito.when(repository.save(animeToUpdate)).thenReturn(animeToUpdate);

        org.assertj.core.api.Assertions.assertThatNoException().isThrownBy(() -> service.update(animeToUpdate));
    }

    @Test
    @DisplayName("update throws ResponseStatusException when anime is not found")
    @Order(10)
    void update_ThrowsResponseStatusException_WhenAnimeIsNotFound() {
        var animeToUpdate = animeList.getFirst();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(animeToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }
}