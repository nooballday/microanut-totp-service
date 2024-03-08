package totp.service;

import jakarta.inject.Singleton;
import totp.service.model.LoginForm;
import totp.service.model.RegisterForm;
import totp.service.model.User;

import java.util.Optional;
import java.util.Random;

@Singleton
public class AuthService {

    private final Storage storage;

    public  AuthService(Storage storage) {
        this.storage = storage;
    }

    public User register(RegisterForm registerForm) {
        String generatedSecretKey = generateRandomString(32);
        User user = new User(registerForm.getUsername(), registerForm.getPassword(), generatedSecretKey);
        storage.addUser(user);
        return user;
    }

    //might return null
    public String login(LoginForm loginForm) {
        Optional<User> foundUser = storage.getUser(loginForm.getUsername());
        if (foundUser.isEmpty()) {
            return "No user found";
        }

        if (!foundUser.get().getPassword().equals(loginForm.getPassword())) {
            return "Incorrect password";
        }

        if (!TOTP.validate(foundUser.get().getSecretKey(), loginForm.getTOTPToken())) {
            return "Incorrect OTP";
        }

        return "You are logged in!";
    }

    private String generateRandomString(int length) {
        int leftLimit = 48; //ascii character 0
        int rightLimit = 122; //ascii character z
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }
}
