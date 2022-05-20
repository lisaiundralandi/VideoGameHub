package io.github.lisaiundralandi;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {
    PasswordUtil passwordUtil = new PasswordUtil();

    @Test
    void shouldThrowExceptionWhenPasswordIsTooShort() {
        String password = "Pas2!";
        String message = assertThrows(ResponseStatusException.class,
                () -> passwordUtil.checkPassword(password, password)).getMessage();
        assertEquals("400 BAD_REQUEST \"Password too short\"", message);
    }

    @Test
    void shouldThrowExceptionWhenHasNoUpperCaseCharacters() {
        String password = "password222!";
        String message = assertThrows(ResponseStatusException.class,
                () -> passwordUtil.checkPassword(password, password)).getMessage();
        assertEquals("400 BAD_REQUEST \"Password has no upper case characters\"", message);
    }

    @Test
    void shouldThrowExceptionWhenHasNoLowerCaseCharacters() {
        String password = "PASSWORD222!";
        String message = assertThrows(ResponseStatusException.class,
                () -> passwordUtil.checkPassword(password, password)).getMessage();
        assertEquals("400 BAD_REQUEST \"Password has no lower case characters\"", message);
    }

    @Test
    void shouldThrowExceptionWhenHasNoDigits() {
        String password = "Password!";
        String message = assertThrows(ResponseStatusException.class,
                () -> passwordUtil.checkPassword(password, password)).getMessage();
        assertEquals("400 BAD_REQUEST \"Password has no digits\"", message);
    }

    @Test
    void shouldThrowExceptionWhenHasNoSpecialCharacters() {
        String password = "Password2";
        String message = assertThrows(ResponseStatusException.class,
                () -> passwordUtil.checkPassword(password, password)).getMessage();
        assertEquals("400 BAD_REQUEST \"Password has no special characters\"", message);
    }

    @Test
    void shouldThrowExceptionWhenPasswordsDoNotMatch() {
        String password = "Password2!";
        String passwordConfirmations = "Password22!";
        String message = assertThrows(ResponseStatusException.class,
                () -> passwordUtil.checkPassword(password, passwordConfirmations)).getMessage();
        assertEquals("400 BAD_REQUEST \"Passwords do not match\"", message);
    }

    @Test
    void shouldAssertTrueWhenPasswordMeetsRequirements() {
        String password = "Password2!";
        assertDoesNotThrow(() -> passwordUtil.checkPassword(password, password));
    }

    @Test
    void shouldHashPassword() {
        byte[] hash = passwordUtil.hash("Password2!");
        byte[] expected = {15, -101, -78, -60, 111, 38, -60, -66, 12, -128, -48, -90, 22, 2, -19, -120, 50, -56, 2,
                -38, -70, -4, 102, -25, 126, 54, 24, -36, -121, -61, 76, 107};
        assertArrayEquals(expected, hash);
    }
}