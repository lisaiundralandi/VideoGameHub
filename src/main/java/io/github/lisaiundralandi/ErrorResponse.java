package io.github.lisaiundralandi;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
