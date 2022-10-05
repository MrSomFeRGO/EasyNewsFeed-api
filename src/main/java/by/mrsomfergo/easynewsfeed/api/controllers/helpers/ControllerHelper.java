package by.mrsomfergo.easynewsfeed.api.controllers.helpers;

import by.mrsomfergo.easynewsfeed.api.exceptions.NotFoundException;
import by.mrsomfergo.easynewsfeed.store.entities.UserEntity;
import by.mrsomfergo.easynewsfeed.store.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@Transactional
public class ControllerHelper {

    UserRepository userRepository;

    public UserEntity getUserOrThrowException(Long userId) {

        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "User with \"%s\" doesn't exist.",
                                        userId
                                )
                        )
                );
    }
}
