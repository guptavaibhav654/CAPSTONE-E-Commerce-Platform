package repository;

import model.User;

import java.util.*;

public class UserRepository {

    private final Map<String, User> users = new HashMap<>();
    private int idCounter = 1;

    public boolean save(String name,String email,String password){

        if(users.containsKey(email))
            return false;

        users.put(email,new User(idCounter++,name,email,password));
        return true;
    }

    public Optional<User> findByEmail(String email){
        return Optional.ofNullable(users.get(email));
    }

    public Collection<User> findAll(){
        return users.values();
    }
}
