package io.github.lisaiundralandi.user;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getLogin(), user);
    }

    public boolean doesLoginExist(String login) {
        return users.containsKey(login);
    }

    @Nullable
    public User getUser(String login) {
        return users.get(login);
    }

    public void deleteUser(String login) {
        users.remove(login);
    }

}
