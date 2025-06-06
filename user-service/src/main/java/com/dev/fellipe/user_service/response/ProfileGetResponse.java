package com.dev.fellipe.user_service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProfileGetResponse {
    private Long id;
    private String name;
    private String description;
}
