package by.mrsomfergo.easynewsfeed.store.repositories;

import by.mrsomfergo.easynewsfeed.store.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, Long> {
}
