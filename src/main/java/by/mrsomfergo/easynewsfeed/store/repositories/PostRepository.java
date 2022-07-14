package by.mrsomfergo.easynewsfeed.store.repositories;

import by.mrsomfergo.easynewsfeed.store.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    Stream<PostEntity> streamAllById(Long id);

    Stream<PostEntity> streamAllByAuthorId(Long authorId);

    Stream<PostEntity> streamAllBy();
}
