package io.github.lisaiundralandi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class Game {
    private int id;
    private String title;
    private String creator;
    private String publisher;
    private int yearOfPublishing;
    private List<String> ageRating;
    private String category;
    private String description;
    private String addedBy;
}
