package io.github.lisaiundralandi.game;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchQuery {
    private String title;
    private String creator;
    private String publisher;
    private String platform;
    private int yearOfPublishing;
    private String category;
}
