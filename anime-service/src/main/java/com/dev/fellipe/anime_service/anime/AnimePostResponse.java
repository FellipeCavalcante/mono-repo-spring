package com.dev.fellipe.anime_service.anime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AnimePostResponse {
    private Long id;

    private String name;
}
