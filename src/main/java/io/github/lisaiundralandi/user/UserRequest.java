package io.github.lisaiundralandi.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRequest {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirmations;
}
