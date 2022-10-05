package by.mrsomfergo.easynewsfeed.api.factories;

import by.mrsomfergo.easynewsfeed.api.dto.CommentDto;
import by.mrsomfergo.easynewsfeed.store.entities.CommentEntity;
import org.springframework.stereotype.Component;

@Component
public class CommentDtoFactory {

    public CommentDto makeCommentDto(CommentEntity entity){
        return CommentDto.builder()
                .id(entity.getId())
                .content(entity.getContent())
                .like(entity.getLikes())
                .dislike(entity.getDislikes())
                .createdAt(entity.getCreatedAt())
                .authorId(entity.getAuthorId())
                .build();
    }
}
