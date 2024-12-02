package com.dev.fellipe.anime_service.domain;

import lombok.Getter;

import java.util.List;

@Getter
public class Anime {
    private Long id;
    private String name;

    public Anime(String name, Long id) {
        this.name = name;
        this.id = id;
    }

    public static List<Anime> getAnime() {
        var itachi = new Anime("Itachi", 1L);
        var gojou = new Anime("Satoru Gojou", 2L);
        var tanjiro = new Anime("Tanjiro", 3L);

        return List.of(itachi, gojou, tanjiro);
    }
}
