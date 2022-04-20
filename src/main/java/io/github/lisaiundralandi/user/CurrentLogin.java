package io.github.lisaiundralandi.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@SessionScope
@Component
@Getter
@Setter(AccessLevel.PACKAGE)
public class CurrentLogin {
    private boolean logged = false;
    private String login = null;

}
