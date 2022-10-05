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
@Table(name = "news_feed_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    @Column(unique = true)
    String login;

    String password;

    @Column(unique = true)
    String name;

    @Builder.Default
    Double rating = 0.0;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    List<PostEntity> posts = new ArrayList<>();
}
