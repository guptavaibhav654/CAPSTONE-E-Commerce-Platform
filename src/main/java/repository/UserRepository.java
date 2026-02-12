package repository;

import model.User;
import factory.UserFactory;
import java.util.*;

public class UserRepository {

    private final Map<String, User> users = new HashMap<>();
    private int idCounter = 1;

    public boolean save(String name,String email,String password){

        if(users.containsKey(email))
            return false;

        User user = UserFactory.createUser(name,email,password);
        users.put(email,user);
        return true;
    }

    public Optional<User> findByEmail(String email){
        return Optional.ofNullable(users.get(email));
    }

    public Collection<User> findAll(){
        return users.values();
    }
}
