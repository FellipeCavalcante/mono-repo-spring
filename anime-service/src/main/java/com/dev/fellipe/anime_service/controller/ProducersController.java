package com.dev.fellipe.anime_service.controller;

import com.dev.fellipe.anime_service.mapper.ProducerMapper;
import com.dev.fellipe.anime_service.request.ProducerPostRequest;
import com.dev.fellipe.anime_service.request.ProducerPutRequest;
import com.dev.fellipe.anime_service.response.ProducerGetResponse;
import com.dev.fellipe.anime_service.response.ProducerPostResponse;
import com.dev.fellipe.anime_service.service.ProducerService;
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
    private ProducerService service;

    public ProducersController() {
        this.service = new ProducerService();
    }

    @GetMapping()
    public ResponseEntity<List<ProducerGetResponse>> listAll(@RequestParam(required = false) String name) {
        log.debug("Request recived to list all producers, param name '{}'", name);

        var producers = service.findAll(name);
        var producerGetResponse = MAPPER.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponse);
    }


    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        var producer = service.findByIdOrThrowNotFound(id);
        var producerGetResponse = MAPPER.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProducerPostResponse> save(@RequestBody ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("Headers received: {}", headers);

        var producer = MAPPER.toProducer(producerPostRequest);
        var producerSaved = service.save(producer);
        var producerGetResponse = MAPPER.toProducerPostResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerGetResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete producer by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody ProducerPutRequest request) {
        log.debug("Request to update producer {}", request);

        var producerToUpdate = MAPPER.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }
}
