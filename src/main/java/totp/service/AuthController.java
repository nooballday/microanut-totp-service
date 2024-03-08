package totp.service;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import totp.service.model.LoginForm;
import totp.service.model.RegisterForm;
import totp.service.model.User;

@Controller("auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Post(value = "/register", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    User register(@Body RegisterForm registerForm) {
        if (registerForm.getUsername() == null || registerForm.getPassword() == null) {
            return null;
        }
        return authService.register(registerForm);
    }

    @Post(value = "/login", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    String login(@Body LoginForm loginForm) {
        return authService.login(loginForm);
    }
}
