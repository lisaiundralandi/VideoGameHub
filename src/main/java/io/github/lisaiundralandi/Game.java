package io.github.lisaiundralandi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class Game {
    private int id;
    private String title;
    private String creator;
    private String publisher;
    private int yearOfPublishing;
    private List<String> ageRating;
    private String category;
    private String description;
}
