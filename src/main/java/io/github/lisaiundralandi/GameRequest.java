package io.github.lisaiundralandi;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class GameRequest {

    @NotBlank
    private String title;
    @NotNull
    private String creator;
    @NotNull
    private String publisher;
    private List<String> ageRating;
    private String category;
    private String description;
}
