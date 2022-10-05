package by.mrsomfergo.easynewsfeed.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentDto {

    @NonNull
    Long id;

    @NonNull
    String content;

    @NonNull
    Integer like;

    @NonNull
    Integer dislike;

    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;

    @NonNull
    @JsonProperty("author_id")
    Long authorId;
}
