package io.github.lisaiundralandi.user;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserRequest {
    @NotBlank
    private String login;
    @NotBlank
    private String password;
    @NotBlank
    private String passwordConfirmations;
}
