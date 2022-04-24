package io.github.lisaiundralandi.user;

import io.github.lisaiundralandi.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.Objects;

@SessionScope
@Component
@Getter
@Setter(AccessLevel.PACKAGE)
public class CurrentLogin {
    private boolean logged = false;
    private User user = null;

    public String getLogin() {
        return Objects.requireNonNull(user, "User not logged in")
                .getLogin();
    }
}
