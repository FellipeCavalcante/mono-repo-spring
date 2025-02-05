package com.dev.fellipe.anime_service.repository;

import com.dev.fellipe.anime_service.domain.Producer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProducerHardCodedRepositoryTest {
    @InjectMocks
    private ProducerHardCodedRepository repository;

    @Mock
    private ProducerData producerData;
    private List<Producer> producerList;

    @BeforeEach
    void init() {
        var mappa = com.dev.fellipe.anime_service.domain.Producer.builder().id(1L).name("Ufotable").createdAt(LocalDateTime.now()).build();
        var witStudio = com.dev.fellipe.anime_service.domain.Producer.builder().id(2L).name("Wit Studio").createdAt(LocalDateTime.now()).build();
        var studioGhibli = com.dev.fellipe.anime_service.domain.Producer.builder().id(3L).name("Studio Ghibli").createdAt(LocalDateTime.now()).build();
        producerList = new ArrayList<>(List.of(mappa, studioGhibli, witStudio));
    }

    @Test
    @DisplayName("findAll returns a lost with all producers")
    void findAll_ReturnAllProducers_WhenSucessul() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findAll();
        Assertions.assertThat(producers).hasSize(producers.size());
    }

    @Test
    @DisplayName("findById returns a producers with given id")
    void findById_ReturnProducersById_WhenSucessul() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducer = producerList.getFirst();
        var producers = repository.findById(expectedProducer.getId());
        Assertions.assertThat(producers).isPresent().contains(expectedProducer);
    }

    @Test
    @DisplayName("findByName returns empty list when name is null")
    void findByName_ReturnEmptyList_WhenNameIsNull() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producers = repository.findByName(null);
        Assertions.assertThat(producers).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("findByName returns list with found object when name exists")
    void findByName_ReturnsProducerList_WhenNameExists() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var expectedProducer = producerList.getFirst();
        var producers = repository.findByName(expectedProducer.getName());
        Assertions.assertThat(producers).contains(expectedProducer);
    }

    @Test
    @DisplayName("Save creates a producer")
    void save_CreatesProducer_WhenSucessul() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToSave = Producer.builder()
                .id(99L)
                .name("test")
                .createdAt(LocalDateTime.now())
                .build();
        var producer = repository.save(producerToSave);
        Assertions.assertThat(producer).isEqualTo(producerToSave).hasNoNullFieldsOrProperties();

        var producerSavedOptional = repository.findById(producerToSave.getId());
        Assertions.assertThat(producerSavedOptional).isPresent().contains(producerToSave);
    }

    @Test
    @DisplayName("delete remove a producer")
    void delete_RemoveProducer_WhenSucessul() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var ProducerToDelete = producerList.getFirst();
        repository.delete(ProducerToDelete);
        Assertions.assertThat(this.producerList).doesNotContain(ProducerToDelete);
    }

    @Test
    @DisplayName("update updates a producer")
    void update_UpdatesProducer_WhenSucessul() {
        BDDMockito.when(producerData.getProducers()).thenReturn(producerList);

        var producerToUpdate = this.producerList.getFirst();
        producerToUpdate.setName("updatedName");
        repository.update(producerToUpdate);
        Assertions.assertThat(this.producerList).contains(producerToUpdate);

        var producerUpdatedOptional = repository.findById(producerToUpdate.getId());
        Assertions.assertThat(producerUpdatedOptional).isPresent();
        Assertions.assertThat(producerUpdatedOptional.get().getName()).isEqualTo("updatedName");
    }
}