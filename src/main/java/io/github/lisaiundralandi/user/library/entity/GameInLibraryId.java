package io.github.lisaiundralandi.user.library.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class GameInLibraryId implements Serializable {

    private String userId;

    private long gameId;
}
