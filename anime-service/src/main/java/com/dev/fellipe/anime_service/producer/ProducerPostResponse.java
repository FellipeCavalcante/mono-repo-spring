package com.dev.fellipe.anime_service.producer;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProducerPostResponse {
    private Long id;
    private String name;
}
