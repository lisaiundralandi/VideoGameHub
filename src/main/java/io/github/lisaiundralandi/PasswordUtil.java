package io.github.lisaiundralandi;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordUtil {

    public void checkPassword(String password, String passwordConfirmations) {
        boolean hasNoUpperCase = password.chars()
                .noneMatch(Character::isUpperCase);
        boolean hasNoLowerCase = password.chars()
                .noneMatch(Character::isLowerCase);
        boolean hasNoDigits = password.chars()
                .noneMatch(Character::isDigit);
        boolean hasNoSpecialChars = password.chars()
                .allMatch(Character::isLetterOrDigit);

        if (password.length() <= 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password too short");
        } else if (hasNoUpperCase) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password has no upper case characters");
        } else if (hasNoLowerCase) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password has no lower case characters");
        } else if (hasNoDigits) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password has no digits");
        } else if (hasNoSpecialChars) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password has no special characters");
        }

        if (!password.equals(passwordConfirmations)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passwords do not match");
        }
    }

    public byte[] hash(String password) {
        try {

            MessageDigest md = MessageDigest.getInstance("SHA3-256");
            return md.digest(password.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
