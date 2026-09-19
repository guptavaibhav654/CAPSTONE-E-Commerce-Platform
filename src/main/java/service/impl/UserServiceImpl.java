package service.impl;

import model.User;
import repository.UserRepository;
import service.UserService;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo){
        this.repo = repo;
    }

    private boolean isValidEmail(String email){
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isValidName(String name){
        return name.matches("[A-Za-z ]{3,}");
    }

    @Override
    public boolean register(String name,String email,String password){

        if(name.isBlank()||email.isBlank()||password.isBlank()){
            System.out.println("Fields cannot be empty");
            return false;
        }

        if(!isValidName(name)){
            System.out.println("Invalid name");
            return false;
        }

        if(!isValidEmail(email)){
            System.out.println("Invalid email");
            return false;
        }

        boolean saved = repo.save(name,email,password);

        if(!saved){
            System.out.println("Email already exists");
            return false;
        }

        return true;
    }

    @Override
    public Optional<User> login(String email,String password){

        Optional<User> user = repo.findByEmail(email);

        if(user.isPresent() && user.get().getPassword().equals(password))
            return user;

        return Optional.empty();
    }
}
