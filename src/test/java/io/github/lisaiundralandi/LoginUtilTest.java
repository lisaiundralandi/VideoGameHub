package io.github.lisaiundralandi;

import io.github.lisaiundralandi.user.CurrentLogin;
import io.github.lisaiundralandi.user.entity.User;
import io.github.lisaiundralandi.user.entity.UserType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class LoginUtilTest {
    private final CurrentLogin currentLogin = Mockito.mock(CurrentLogin.class);
    private final LoginUtil loginUtil = new LoginUtil(currentLogin);

    @Test
    void shouldThrowExceptionIfNotLogged() {
        HttpStatus status = assertThrows(ResponseStatusException.class, loginUtil::checkIfLogged).getStatus();
        assertEquals(HttpStatus.UNAUTHORIZED, status);
    }

    @Test
    void shouldNotThrowExceptionIfLogged() {
        Mockito.when(currentLogin.isLogged())
                .thenReturn(true);
        assertDoesNotThrow(loginUtil::checkIfLogged);
    }

    @Test
    void shouldThrowExceptionIfNotLoggedForAccessCheck() {
        HttpStatus status = assertThrows(ResponseStatusException.class,
                () -> loginUtil.checkAccess(UserType.ADMIN)).getStatus();
        assertEquals(HttpStatus.UNAUTHORIZED, status);
    }

    @Test
    void shouldThrowExceptionIfUserHasNoAccess() {
        User user = new User();
        user.setType(UserType.STANDARD);

        Mockito.when(currentLogin.isLogged())
                .thenReturn(true);
        Mockito.when(currentLogin.getUser())
                .thenReturn(user);

        String message = assertThrows(ResponseStatusException.class,
                () -> loginUtil.checkAccess(UserType.ADMIN)).getMessage();
        assertEquals("403 FORBIDDEN \"Access prohibited\"", message);
    }

    @Test
    void shouldNotThrowExceptionWhenUserHasAccess() {
        User user = new User();
        user.setType(UserType.ADMIN);

        Mockito.when(currentLogin.isLogged())
                .thenReturn(true);
        Mockito.when(currentLogin.getUser())
                .thenReturn(user);

        assertDoesNotThrow(() -> loginUtil.checkAccess(UserType.ADMIN));
    }
}