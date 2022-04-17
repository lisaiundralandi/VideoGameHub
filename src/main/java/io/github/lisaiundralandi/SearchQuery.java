package io.github.lisaiundralandi;

import lombok.Getter;

import java.util.List;

@Getter
public class SearchQuery {
    private String title;
    private String creator;
    private String publisher;
    private int yearOfPublishing;
    private String category;
}
