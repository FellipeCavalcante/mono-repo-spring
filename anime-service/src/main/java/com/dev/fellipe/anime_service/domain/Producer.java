package com.dev.fellipe.anime_service.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Producer {
    private Long id;
    @JsonProperty("full_name")
    private String name;

    private static List<Producer> producers = new ArrayList<>();
    static {
        var mappa = new Producer(1L, "Mappa");
        var kyotoAnimation = new Producer(2L, "Kyoto Animation");
        var madhouse = new Producer(3L, "Madhouse");
        producers.addAll(List.of(mappa, madhouse, kyotoAnimation));
    }

    public static List<Producer> getProducers() {
        return producers;
    }
}
