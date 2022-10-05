package by.mrsomfergo.easynewsfeed.api.factories;

import by.mrsomfergo.easynewsfeed.api.dto.UserDto;
import by.mrsomfergo.easynewsfeed.store.entities.UserEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class UserDtoFactory {

    PostDtoFactory postFactory;

    public UserDto makeUserDto(UserEntity entity){
        return UserDto.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .name(entity.getName())
                .rating(entity.getRating())
                .createdAt(entity.getCreatedAt())
                .posts(entity
                        .getPosts()
                        .stream()
                        .map(postFactory::makePostDto)
                        .collect(Collectors.toList())
                )
                .build();
    }
}
