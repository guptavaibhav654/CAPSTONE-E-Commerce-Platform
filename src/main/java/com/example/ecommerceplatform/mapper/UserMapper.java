package com.example.ecommerceplatform.mapper;

import com.example.ecommerceplatform.dto.request.UserRequest;
import com.example.ecommerceplatform.dto.response.UserResponse;
import com.example.ecommerceplatform.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequest request) {

        if (request == null) {
            return null;
        }

        User user = new User();

        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());

        // Password should be encoded in the Service layer
        user.setPassword(request.getPassword());

        return user;
    }

    public static UserResponse toResponse(User user) {

        if (user == null) {
            return null;
        }

        return new UserResponse(
                user.getUserId(),
                user.getUserName(),
                user.getEmail()
        );
    }
}