package com.dev.fellipe.anime_service.producer;

import com.dev.fellipe.anime_service.domain.Producer;
import dev.fellipe.dto.ProducerGetResponse;
import dev.fellipe.dto.ProducerPostRequest;
import dev.fellipe.dto.ProducerPostResponse;
import dev.fellipe.dto.ProducerPutRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProducerMapper {

    Producer toProducer(ProducerPostRequest postRequest);

    Producer toProducer(ProducerPutRequest request);

    ProducerGetResponse toProducerGetResponse(Producer producer);

    ProducerPostResponse toProducerPostResponse(Producer producer);

    List<ProducerGetResponse> toProducerGetResponseList(List<Producer> producers);

}
