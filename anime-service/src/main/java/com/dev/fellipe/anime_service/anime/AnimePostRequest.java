package com.dev.fellipe.anime_service.anime.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnimePostRequest {
    @NotBlank(message = "The field 'name' is required")
    private String name;
}
