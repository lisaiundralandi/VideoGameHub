package io.github.lisaiundralandi.user.library.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum Status {
    OWNED,
    PREOWNED,
    WISHLIST,
    PREORDERED
}
