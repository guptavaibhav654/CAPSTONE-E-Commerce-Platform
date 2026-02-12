package factory;

import model.User;

public class UserFactory {

    private static int idCounter = 1;

    public static User createUser(String name,String email,String password){
        return new User(idCounter++,name,email,password);
    }
}
