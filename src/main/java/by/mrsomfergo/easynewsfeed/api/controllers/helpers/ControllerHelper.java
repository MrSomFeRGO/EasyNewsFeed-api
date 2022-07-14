package by.mrsomfergo.easynewsfeed.api.controllers.helpers;

import by.mrsomfergo.easynewsfeed.api.exceptions.NotFoundException;
import by.mrsomfergo.easynewsfeed.store.entities.CommentEntity;
import by.mrsomfergo.easynewsfeed.store.entities.PostEntity;
import by.mrsomfergo.easynewsfeed.store.entities.UserEntity;
import by.mrsomfergo.easynewsfeed.store.repositories.CommentRepository;
import by.mrsomfergo.easynewsfeed.store.repositories.PostRepository;
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

    PostRepository postRepository;

    CommentRepository commentRepository;

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

    public PostEntity getPostOrThrowException(Long postId){
        return postRepository
                .findById(postId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Post with \"%s\" doesn't exist.",
                                        postId
                                )
                        )
                );
    }

    public CommentEntity getCommentOrThrowException(Long commentId){
        return commentRepository
                .findById(commentId)
                .orElseThrow(() ->
                        new NotFoundException(
                                String.format(
                                        "Comment with \"%s\" doesn't exist.",
                                        commentId
                                )
                        )
                );
    }
}
