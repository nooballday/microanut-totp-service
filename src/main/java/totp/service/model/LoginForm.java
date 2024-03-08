package totp.service.model;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;

@Introspected
@Serdeable
public class LoginForm {
    private String username;
    private String password;
    private String TOTPToken;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTOTPToken() {
        return TOTPToken;
    }

    public void setTOTPToken(String TOTPToken) {
        this.TOTPToken = TOTPToken;
    }
}
