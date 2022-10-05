package by.mrsomfergo.easynewsfeed.store.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "news_feed_comment")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String content;

    //String imageOrVideoUrl

    @Builder.Default
    Integer likes = 0;

    @Builder.Default
    Integer dislikes = 0;

    @Builder.Default
    Instant createdAt = Instant.now();

    Long authorId;
}
