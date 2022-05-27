package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.user.library.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateGameRequest {
    private Double rating;
    private Status status;
    private boolean played;
}
