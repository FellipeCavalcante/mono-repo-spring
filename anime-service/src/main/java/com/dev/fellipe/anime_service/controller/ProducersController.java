package com.dev.fellipe.anime_service.controller;

import com.dev.fellipe.anime_service.domain.Producer;
import com.dev.fellipe.anime_service.mapper.ProducerMapper;
import com.dev.fellipe.anime_service.request.ProducerPostRequest;
import com.dev.fellipe.anime_service.response.ProducerGetResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("v1/producers")
@Slf4j
public class ProducersController {

    private static final ProducerMapper MAPPER = ProducerMapper.INSTACE;

    @GetMapping()
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request recived to list all producers, param name '{}'", name);

        var producers = Producer.getProducers();
        var producerGetResponseList = MAPPER.toProducerGetResponseList(producers);

        if (name == null) return ResponseEntity.ok(producerGetResponseList);

        var response = producerGetResponseList.stream().filter(producer -> producer.getName().equalsIgnoreCase(name)).toList();

        return ResponseEntity.ok(response);
    }


    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        var producerGetResponse =  Producer.getProducers()
                .stream().filter(producers -> producers.getId().equals(id))
                .findFirst()
                .map(MAPPER::toProducerGetResponse)
                .orElse(null);

        return ResponseEntity.ok(producerGetResponse);

    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProducerGetResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("Headers received: {}", headers);
        var producer = MAPPER.toProducer(producerPostRequest);
        var response = MAPPER.toProducerGetResponse(producer);

        Producer.getProducers().add(producer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
