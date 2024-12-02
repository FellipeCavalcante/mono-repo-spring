package com.dev.fellipe.anime_service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Anime {
    private Long id;
    private String name;

    public static List<Anime> getAnime() {
        var itachi = new Anime(1L, "Itachi");
        var gojou = new Anime(2L, "Satoru Gojou");
        var tanjiro = new Anime(3L, "Tanjiro");

        return List.of(itachi, gojou, tanjiro);
    }
}
