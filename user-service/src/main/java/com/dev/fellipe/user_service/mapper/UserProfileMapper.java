package com.dev.fellipe.user_service.mapper;

import com.dev.fellipe.user_service.domain.UserProfile;
import com.dev.fellipe.user_service.response.UserProfileGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    List<UserProfileGetResponse> toUserProfileGetResponse(List<UserProfile> userProfiles);
}
