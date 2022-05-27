package io.github.lisaiundralandi.game.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return id == game.id && yearOfPublishing == game.yearOfPublishing && Objects.equals(title, game.title) &&
                Objects.equals(creator, game.creator) && Objects.equals(publisher, game.publisher) &&
                Objects.equals(platform, game.platform) &&
                Objects.equals(new ArrayList<>(ageRating), new ArrayList<>(game.ageRating)) &&
                Objects.equals(category, game.category) &&
                Objects.equals(description, game.description) && Objects.equals(addedBy, game.addedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, creator, publisher, platform, yearOfPublishing, new ArrayList<>(ageRating),
                category, description,
                addedBy);
    }
}
