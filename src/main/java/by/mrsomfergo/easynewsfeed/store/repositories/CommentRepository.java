package by.mrsomfergo.easynewsfeed.store.repositories;

import by.mrsomfergo.easynewsfeed.store.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
