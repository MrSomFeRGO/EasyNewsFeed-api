package by.mrsomfergo.easynewsfeed.store.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "news_feed_post")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String title;

    String content;

    //String imageOrVideoUrl;

    @Builder.Default
    Integer likes = 0;

    @Builder.Default
    Integer dislikes = 0;

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    List<CommentEntity> comments = new ArrayList<>();

    @Builder.Default
    Instant createdAt = Instant.now();

    Long authorId;

}