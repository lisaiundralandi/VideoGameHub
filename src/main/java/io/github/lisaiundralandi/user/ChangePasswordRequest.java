package io.github.lisaiundralandi.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChangePasswordRequest {
    @NotBlank
    private String password;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String newPasswordConfirmations;
}
