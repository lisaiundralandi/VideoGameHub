package io.github.lisaiundralandi.game.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GenericGenerator(name = "gen", strategy = "increment")
    @GeneratedValue(generator = "gen")
    private long id;
    private String title;
    private String creator;
    private String publisher;
    private String platform;
    private int yearOfPublishing;
    @ElementCollection
    private List<String> ageRating;
    private String category;
    private String description;
    private String addedBy;
}
