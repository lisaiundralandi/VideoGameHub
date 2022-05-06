package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.user.library.entity.Status;
import lombok.Getter;

@Getter
public class UpdateGameRequest {
    private Double rating;
    private Status status;
    private boolean played;
}
