package service;

import model.User;

import java.util.Optional;

public interface UserService {

    boolean register(String name,String email,String password);
    Optional<User> login(String email,String password);
}
