package io.github.lisaiundralandi.user.library;

import io.github.lisaiundralandi.user.library.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AddGameToLibraryRequest {
    private long id;
    @Nullable
    private Double rating;
    @Nullable
    private Status status;
    private boolean played;

}
