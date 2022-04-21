package io.github.lisaiundralandi;

import io.github.lisaiundralandi.user.CurrentLogin;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class LoginUtil {
    private final CurrentLogin currentLogin;

    public LoginUtil(CurrentLogin currentLogin) {
        this.currentLogin = currentLogin;
    }

    public void checkIfLogged() {
        if (!currentLogin.isLogged()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
