package by.mrsomfergo.easynewsfeed.api.controllers;

import by.mrsomfergo.easynewsfeed.api.controllers.helpers.ControllerHelper;
import by.mrsomfergo.easynewsfeed.api.dto.AckDto;
import by.mrsomfergo.easynewsfeed.api.dto.CommentDto;
import by.mrsomfergo.easynewsfeed.api.exceptions.BadRequestException;
import by.mrsomfergo.easynewsfeed.api.factories.CommentDtoFactory;
import by.mrsomfergo.easynewsfeed.store.entities.CommentEntity;
import by.mrsomfergo.easynewsfeed.store.entities.PostEntity;
import by.mrsomfergo.easynewsfeed.store.repositories.CommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class CommentController {

    ControllerHelper controllerHelper;

    CommentRepository commentRepository;

    CommentDtoFactory commentDtoFactory;

    public static final String FETCH_COMMENT = "/api/posts/{post_id}/comments";
    public static final String CREATE_COMMENT = "/api/posts/{post_id}/comments";
    public static final String UPDATE_COMMENT = "/api/comments/{comment_id}";
    public static final String DELETE_COMMENT = "/api/comments/{comment_id}";

    @GetMapping(FETCH_COMMENT)
    public List<CommentDto> fetchComment(
            @PathVariable("post_id") Long postId){

        PostEntity post = controllerHelper.getPostOrThrowException(postId);

        return post
                .getComments()
                .stream()
                .map(commentDtoFactory::makeCommentDto)
                .collect(Collectors.toList());
    }

    @PostMapping(CREATE_COMMENT)
    public CommentDto createComment(
            @PathVariable("post_id") Long postId,
            @RequestParam(name = "comment_content") String commentContent,
            @RequestParam(name = "comment_author_id") Long commentAuthorId){

        if(commentContent.trim().isEmpty()) throw new BadRequestException("Comment content can't be empty.");

        PostEntity post = controllerHelper.getPostOrThrowException(postId);
        CommentEntity comment = CommentEntity
                .builder()
                .content(commentContent)
                .authorId(commentAuthorId)
                .build();

        post.getComments().add(comment);

        CommentEntity savedComment = commentRepository.saveAndFlush(comment);
        return commentDtoFactory.makeCommentDto(savedComment);
    }

    @PatchMapping(UPDATE_COMMENT)
    public CommentDto updatePost(
            @PathVariable(name = "comment_id") Long commentId,
            @RequestParam(name = "cooment_like", required = false) Optional<Integer> optionalCommentLike,
            @RequestParam(name = "cooment_dislike", required = false) Optional<Integer> optionalCommentDislike){

        if(optionalCommentLike.isEmpty() && optionalCommentDislike.isEmpty()) throw new BadRequestException("Like and Dislike can't be empty.");

        CommentEntity comment = controllerHelper.getCommentOrThrowException(commentId);

        optionalCommentLike.ifPresent(postLike -> comment.setLikes(comment.getLikes() + postLike));
        optionalCommentDislike.ifPresent(postDislike -> comment.setDislikes(comment.getDislikes() + postDislike));

        CommentEntity savedComment = commentRepository.saveAndFlush(comment);
        return commentDtoFactory.makeCommentDto(savedComment);
    }

    @DeleteMapping(DELETE_COMMENT)
    public AckDto deleteComment(
            @PathVariable("comment_id") Long commentId){

            CommentEntity comment = controllerHelper.getCommentOrThrowException(commentId);

            commentRepository.delete(comment);

            return AckDto.makeDefault(true);
    }
}
