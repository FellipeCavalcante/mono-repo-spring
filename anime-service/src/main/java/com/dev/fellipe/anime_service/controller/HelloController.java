package com.dev.fellipe.anime_service.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("v1/greetings")
@Slf4j
public class HelloController {

    @GetMapping
    public String hi() {
        return "OMAE WA MOU SHINDE IRU.";
    }

    @PostMapping
    public Long save(@RequestBody String name) {
        log.info("Saving name: {}", name);
        return ThreadLocalRandom.current().nextLong(1, 100);
    }
}
