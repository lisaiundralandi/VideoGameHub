package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.user.library.entity.Status;
import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class AddGameToLibraryRequest {
    private long id;
    @Nullable
    private Double rating;
    @Nullable
    private Status status;

}
