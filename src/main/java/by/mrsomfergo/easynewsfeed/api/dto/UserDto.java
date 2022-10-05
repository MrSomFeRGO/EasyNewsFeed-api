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
public class UserDto {

    @NonNull
    Long id;

    @NonNull
    String login;

    @NonNull
    String password;

    @NonNull
    String name;

    @NonNull
    Double rating;

    @NonNull
    @JsonProperty("created_at")
    Instant createdAt;

    @NonNull
    List<PostDto> posts;
}
