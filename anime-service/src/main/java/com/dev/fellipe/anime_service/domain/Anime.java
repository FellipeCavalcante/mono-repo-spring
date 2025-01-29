package com.dev.fellipe.anime_service.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Anime {
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private static List<Anime> animes = new ArrayList<>();
    static {
        var itachi = new Anime(1L, "Itachi");
        var gojou = new Anime(2L, "Satoru Gojou");
        var tanjiro = new Anime(3L, "Tanjiro");
        animes.addAll(List.of(itachi, gojou, tanjiro));
    }

    public static List<Anime> getAnimes() {
        return animes;
    }
}
