package totp.service;

import jakarta.inject.Singleton;
import totp.service.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class Storage {

    public List<User> users = new ArrayList<>();


    public void addUser(User user) {
        this.users.add(user);
    }

    public Optional<User> getUser(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return users.stream().filter( user -> username.equals(user.getUsername())).findFirst();
    }

}
