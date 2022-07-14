package by.mrsomfergo.easynewsfeed.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostDto {

    @NonNull
    Long id;

    @NonNull
    String title;

    @NonNull
    String content;

    @NonNull
    Integer like;

    @NonNull
    Integer dislike;

    @NonNull
    List<CommentDto> comments;

    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;

    @NonNull
    @JsonProperty("author_id")
    Long authorId;
}
