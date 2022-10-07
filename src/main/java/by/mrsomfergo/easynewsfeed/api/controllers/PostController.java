package by.mrsomfergo.easynewsfeed.api.controllers;

import by.mrsomfergo.easynewsfeed.api.controllers.helpers.ControllerHelper;
import by.mrsomfergo.easynewsfeed.api.dto.AckDto;
import by.mrsomfergo.easynewsfeed.api.dto.PostDto;
import by.mrsomfergo.easynewsfeed.api.exceptions.BadRequestException;
import by.mrsomfergo.easynewsfeed.api.factories.PostDtoFactory;
import by.mrsomfergo.easynewsfeed.store.entities.PostEntity;
import by.mrsomfergo.easynewsfeed.store.entities.UserEntity;
import by.mrsomfergo.easynewsfeed.store.repositories.PostRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@RestController
public class PostController {

    ControllerHelper controllerHelper;

    PostRepository postRepository;

    PostDtoFactory postDtoFactory;

    public static final String FETCH_POST = "/api/posts";
    public static final String CREATE_POST = "/api/posts";
    public static final String UPDATE_POST = "/api/posts/{post_id}";
    public static final String DELETE_POST = "/api/posts/{post_id}";

    @GetMapping(FETCH_POST)
    public List<PostDto> fetchPosts(
            @RequestParam(name = "post_id", required = false) Optional<Long> optionalPostId,
            @RequestParam(name = "author_id", required = false) Optional<Long> optionalAuthorId){

        if(optionalAuthorId.isEmpty() & optionalPostId.isEmpty()){
            return postRepository
                    .streamAllBy()
                    .map(postDtoFactory::makePostDto)
                    .collect(Collectors.toList());
        }
        return optionalPostId.map(postId -> Collections.singletonList(postDtoFactory.makePostDto(
                controllerHelper.getPostOrThrowException(postId))))
                .orElseGet(() -> postRepository
                .streamAllByAuthorId(optionalAuthorId.get())
                .map(postDtoFactory::makePostDto)
                .collect(Collectors.toList()));
    }

    @PostMapping(CREATE_POST)
    public PostDto createPost(
            @RequestParam(name = "post_title") String postTitle,
            @RequestParam(name = "post_content") String postContent,
            @RequestParam(name = "post_author_id") Long postAuthorId,
            @RequestParam(name = "post_file_url", required = false) Optional<String> optionalPostFileUrl){

        optionalPostFileUrl = optionalPostFileUrl.filter(postFileUrl -> !postFileUrl.trim().isEmpty());

        if(postTitle.trim().isEmpty()){
            throw new BadRequestException("Post title can't be empty.");
        } else if (postContent.trim().isEmpty()){
            throw new BadRequestException("Post content can't be empty.");
        }

        UserEntity user = controllerHelper.getUserOrThrowException(postAuthorId);
        PostEntity.PostEntityBuilder postBuilder = PostEntity
                .builder()
                .title(postTitle)
                .authorId(postAuthorId)
                .content(postContent);
        optionalPostFileUrl
                .ifPresent(postBuilder::imageOrVideoUrl);

        PostEntity post = postBuilder.build();
        user.getPosts().add(post);

        PostEntity savedPost = postRepository.saveAndFlush(post);

        return postDtoFactory.makePostDto(savedPost);
    }

    @PatchMapping(UPDATE_POST)
    public PostDto updatePost(
            @PathVariable(name = "post_id") Long postId,
            @RequestParam(name = "post_like", required = false) Optional<Integer> optionalPostLike,
            @RequestParam(name = "post_dislike", required = false) Optional<Integer> optionalPostDislike){

        if(optionalPostLike.isEmpty() & optionalPostDislike.isEmpty()) throw new BadRequestException("Like and Dislike can't be empty.");

        PostEntity post = controllerHelper.getPostOrThrowException(postId);

        optionalPostLike.ifPresent(postLike -> post.setLikes(post.getLikes() + postLike));
        optionalPostDislike.ifPresent(postDislike -> post.setDislikes(post.getDislikes() + postDislike));

        PostEntity savedPost = postRepository.saveAndFlush(post);
        return postDtoFactory.makePostDto(savedPost);
    }

    @DeleteMapping(DELETE_POST)
    public AckDto deletePost(
            @PathVariable(name = "post_id") Long postId){

        PostEntity post = controllerHelper.getPostOrThrowException(postId);

        postRepository.delete(post);

        return AckDto.makeDefault(true);
    }
}
