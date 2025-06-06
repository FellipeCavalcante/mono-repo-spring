package com.dev.fellipe.user_service.mapper;

import com.dev.fellipe.user_service.domain.Profile;
import com.dev.fellipe.user_service.request.ProfilePostRequest;
import com.dev.fellipe.user_service.response.ProfileGetResponse;
import com.dev.fellipe.user_service.response.ProfilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {

    Profile toProfile(ProfilePostRequest postRequest);

    ProfilePostResponse toProfilePostResponse(Profile profile);

    ProfileGetResponse toProfileGetResponse(Profile profile);

    List<ProfileGetResponse> toProfileGetResponseList(List<Profile> profiles);
}
