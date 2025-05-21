package com.dev.fellipe.anime_service.producer;

import com.dev.fellipe.anime_service.domain.Producer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProducerData {
    private final List<Producer> producers = new ArrayList<>();

    {
        var mappa = com.dev.fellipe.anime_service.domain.Producer.builder().id(1L).name("Mappa").createdAt(LocalDateTime.now()).build();
        var kyotoAnimation = com.dev.fellipe.anime_service.domain.Producer.builder().id(2L).name("Kyoto Animation").createdAt(LocalDateTime.now()).build();
        var madhouse = com.dev.fellipe.anime_service.domain.Producer.builder().id(3L).name("Madhouse").createdAt(LocalDateTime.now()).build();
        producers.addAll(List.of(mappa, madhouse, kyotoAnimation));
    }

    public List<Producer> getProducers() {
        return producers;
    }
}
