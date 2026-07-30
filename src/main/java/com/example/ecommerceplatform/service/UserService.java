package com.example.ecommerceplatform.service;

import com.example.ecommerceplatform.dto.request.AddCartRequest;
import com.example.ecommerceplatform.dto.request.LoginRequest;
import com.example.ecommerceplatform.dto.request.UserRequest;
import com.example.ecommerceplatform.dto.response.CartResponse;
import com.example.ecommerceplatform.dto.response.UserResponse;
import com.example.ecommerceplatform.model.User;

import java.util.List;

public interface UserService {

    UserResponse login(LoginRequest request);

    UserResponse register(UserRequest user);

    List<UserResponse> getAllUser();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id,UserRequest user);

    void deleteUser(Long id);
}
