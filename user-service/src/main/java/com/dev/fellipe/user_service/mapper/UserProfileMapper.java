package com.dev.fellipe.user_service.mapper;

import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.domain.UserProfile;
import com.dev.fellipe.user_service.response.UserProfileGetResponse;
import com.dev.fellipe.user_service.response.UserProfileUserGetResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

  List<UserProfileGetResponse> toUserProfileGetResponse(
      List<UserProfile> userProfiles);

  List<UserProfileUserGetResponse> toUserProfileUserGetResponseList(
      List<User> users);
}
