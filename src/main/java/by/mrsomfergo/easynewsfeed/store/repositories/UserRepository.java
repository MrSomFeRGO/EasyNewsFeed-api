package by.mrsomfergo.easynewsfeed.store.repositories;

import by.mrsomfergo.easynewsfeed.store.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.stream.Stream;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Stream<UserEntity> streamAllBy();

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByName(String name);
}
