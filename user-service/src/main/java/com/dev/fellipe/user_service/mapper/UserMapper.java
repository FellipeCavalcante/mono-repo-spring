package com.dev.fellipe.user_service.mapper;

import com.dev.fellipe.user_service.domain.User;
import com.dev.fellipe.user_service.request.UserPostRequest;
import com.dev.fellipe.user_service.request.UserPutRequest;
import com.dev.fellipe.user_service.response.UserGetResponse;
import com.dev.fellipe.user_service.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserPostRequest postRequest);

    User toUser(UserPutRequest request);

    UserPostResponse toUserPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);
}
