package com.dev.fellipe.anime_service.anime;

import com.dev.fellipe.anime_service.domain.Anime;
import dev.fellipe.dto.AnimeGetResponse;
import dev.fellipe.dto.AnimePostRequest;
import dev.fellipe.dto.AnimePostResponse;
import dev.fellipe.dto.AnimePutRequest;
import dev.fellipe.dto.PageAnimeGetResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {

  AnimeMapper INSTACE = Mappers.getMapper(
      AnimeMapper.class);

  @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(100_000))")
  Anime toAnime(
      AnimePostRequest postRequest);

  Anime toAnime(
      AnimePutRequest request);

  AnimePostResponse toAnimePostResponse(
      Anime anime);

  AnimeGetResponse toAnimeGetResponse(
      Anime anime);

  List<AnimeGetResponse> toAnimeGetResponseList(
      List<Anime> animes);

  PageAnimeGetResponse toPageAnimeGetResponse(
      Page<Anime> jpaPageAnimeGetResponse);
}
