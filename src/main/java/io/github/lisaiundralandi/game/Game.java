package io.github.lisaiundralandi.game;

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
    private String platform;
    private int yearOfPublishing;
    private List<String> ageRating;
    private String category;
    private String description;
    private String addedBy;
}
