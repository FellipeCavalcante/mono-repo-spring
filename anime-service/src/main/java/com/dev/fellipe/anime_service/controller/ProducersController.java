package com.dev.fellipe.anime_service.controller;

import com.dev.fellipe.anime_service.domain.Anime;
import com.dev.fellipe.anime_service.domain.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController()
@RequestMapping("v1/producers")
@Slf4j
public class ProducersController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Producer> listAll(@RequestParam(required = false) String name) {
        var producer = Producer.getProducers();

        if (name != null) {
            return producer.stream().filter(producers -> producers.getName().equalsIgnoreCase(name)).toList();
        }

        return producer;
    }


    @GetMapping("{id}")
    public Producer findById(@PathVariable Long id) {
        return Producer.getProducers()
                .stream().filter(producers -> producers.getId().equals(id))
                .findFirst().orElse(null);

    }


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Producer> save(@RequestBody Producer producer, @RequestHeader HttpHeaders headers) {
        log.info("Headers received: {}", headers);
        producer.setId(ThreadLocalRandom.current().nextLong(100_000));
        Producer.getProducers().add(producer);

        var responseHearders = new HttpHeaders();
        responseHearders.setContentType(MediaType.APPLICATION_JSON);
        responseHearders.add("Authorization", "My key");

        return ResponseEntity.status(HttpStatus.CREATED).body(producer);
    }
}
