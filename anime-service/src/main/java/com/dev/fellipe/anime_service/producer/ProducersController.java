package com.dev.fellipe.anime_service.producer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ProducersController {

    private final ProducerMapper mapper;
    private final ProducerService service;

    @GetMapping()
    public ResponseEntity<List<ProducerGetResponse>> findAll(@RequestParam(required = false) String name) {
        log.debug("Request recived to list all producers, param name '{}'", name);

        var producers = service.findAll(name);
        var producerGetResponse = mapper.toProducerGetResponseList(producers);

        return ResponseEntity.ok(producerGetResponse);
    }


    @GetMapping("{id}")
    public ResponseEntity<ProducerGetResponse> findById(@PathVariable Long id) {
        log.debug("Request to find producer by id: {}", id);

        var producer = service.findByIdOrThrowNotFound(id);
        var producerGetResponse = mapper.toProducerGetResponse(producer);

        return ResponseEntity.ok(producerGetResponse);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProducerPostResponse> save(@RequestBody @Valid ProducerPostRequest producerPostRequest, @RequestHeader HttpHeaders headers) {
        log.info("Headers received: {}", headers);

        var producer = mapper.toProducer(producerPostRequest);
        var producerSaved = service.save(producer);
        var producerPostResponse = mapper.toProducerPostResponse(producerSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(producerPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        log.debug("Request to delete producer by id: {}", id);

        service.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<Void> update(@RequestBody @Valid ProducerPutRequest request) {
        log.debug("Request to update producer {}", request);

        var producerToUpdate = mapper.toProducer(request);

        service.update(producerToUpdate);

        return ResponseEntity.noContent().build();
    }
}
