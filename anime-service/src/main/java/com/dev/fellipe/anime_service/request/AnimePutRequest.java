package com.dev.fellipe.anime_service.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnimePutRequest {
    private Long id;
    private String name;
}
