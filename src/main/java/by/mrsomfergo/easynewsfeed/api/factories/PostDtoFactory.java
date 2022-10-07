package by.mrsomfergo.easynewsfeed.api.factories;

import by.mrsomfergo.easynewsfeed.api.dto.PostDto;
import by.mrsomfergo.easynewsfeed.store.entities.PostEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class PostDtoFactory {

    CommentDtoFactory commentFactory;

    public PostDto makePostDto(PostEntity entity){
        return PostDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .imageOrVideoUrl(entity.getImageOrVideoUrl())
                .like(entity.getLikes())
                .dislike(entity.getDislikes())
                .comments(entity
                        .getComments()
                        .stream()
                        .map(commentFactory::makeCommentDto)
                        .collect(Collectors.toList())
                )
                .createdAt(entity.getCreatedAt())
                .authorId(entity.getAuthorId())
                .build();
    }
}
