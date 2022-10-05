package by.mrsomfergo.easynewsfeed.api.controllers;

import by.mrsomfergo.easynewsfeed.api.controllers.helpers.ControllerHelper;
import by.mrsomfergo.easynewsfeed.api.dto.AckDto;
import by.mrsomfergo.easynewsfeed.api.dto.UserDto;
import by.mrsomfergo.easynewsfeed.api.exceptions.BadRequestException;
import by.mrsomfergo.easynewsfeed.api.factories.UserDtoFactory;
import by.mrsomfergo.easynewsfeed.store.entities.UserEntity;
import by.mrsomfergo.easynewsfeed.store.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class UserController {

    ControllerHelper controllerHelper;

    UserRepository userRepository;

    UserDtoFactory userDtoFactory;

    private static final String FETCH_USERS = "/api/users";
    public static final String CREATE_OR_UPDATE_USER = "/api/users";
    public static final String DELETE_USER = "/api/users/{user_id}";

    @GetMapping(FETCH_USERS)
    public List<UserDto> fetchUsers(
            @RequestParam(value = "user_id", required = false) Optional<Long> optionalUserId){

        if(optionalUserId.isEmpty()) return userRepository
                .streamAllBy()
                .map(userDtoFactory::makeUserDto)
                .collect(Collectors.toList());

        return Collections.singletonList(
                userDtoFactory.makeUserDto(
                        controllerHelper.getUserOrThrowException(optionalUserId.get())
                )
        );
    }

    @PutMapping(CREATE_OR_UPDATE_USER)
    public UserDto createOrUpdateUser(
            @RequestParam(name = "user_id", required = false) Optional<Long> optionalUserId,
            @RequestParam(name = "user_login", required = false) Optional<String> optionalUserLogin,
            @RequestParam(name = "user_password",required = false) Optional<String> optionalUserPassword,
            @RequestParam(name = "user_name", required = false) Optional<String> optionalUserName,
            @RequestParam(name = "user_rating", required = false) Optional<Double> optionalUserRating){

        optionalUserLogin = optionalUserLogin.filter(userLogin -> !userLogin.trim().isEmpty());
        optionalUserPassword = optionalUserPassword.filter(userPassword -> !userPassword.trim().isEmpty());
        optionalUserName = optionalUserName.filter(userName -> !userName.trim().isEmpty());

        boolean isCreate = optionalUserId.isEmpty();

        if(isCreate && optionalUserLogin.isEmpty()){
            throw new BadRequestException("Login can't be empty.");
        }
        if(isCreate && optionalUserPassword.isEmpty()){
            throw new BadRequestException("Password can't be empty.");
        }
        if(isCreate && optionalUserName.isEmpty()){
            throw new BadRequestException("Name can't be empty.");
        }

        final UserEntity user = optionalUserId
                .map(controllerHelper::getUserOrThrowException)
                .orElseGet(() -> UserEntity.builder().build());

        optionalUserLogin
                .ifPresent(userLogin -> {

                    userRepository
                            .findByLogin(userLogin)
                            .filter(anotherUser -> !Objects.equals(anotherUser.getId(), user.getId()))
                            .ifPresent(anotherUser -> {
                                throw new BadRequestException(String.format(
                                        "Login \"%s\" already exists.",
                                        userLogin
                                ));
                            });

                    user.setLogin(userLogin);
                });

        optionalUserPassword
                .ifPresent(user::setPassword);

        optionalUserName
                .ifPresent(userName -> {

                    userRepository
                            .findByName(userName)
                            .filter(anotherUser -> !Objects.equals(anotherUser.getId(), user.getId()))
                            .ifPresent(anotherUser -> {
                                throw new BadRequestException(String.format(
                                        "Name \"%s\" already exists.",
                                        userName
                                ));
                            });

                    user.setName(userName);
                });

        optionalUserRating
                .ifPresent(user::setRating);

        UserEntity savedUser = userRepository.saveAndFlush(user);

        return userDtoFactory.makeUserDto(savedUser);
    }

    @DeleteMapping(DELETE_USER)
    public AckDto deleteUser(@PathVariable("user_id") Long userId){

        controllerHelper.getUserOrThrowException(userId);

        userRepository.deleteById(userId);

        return AckDto.makeDefault(true);
    }

}
