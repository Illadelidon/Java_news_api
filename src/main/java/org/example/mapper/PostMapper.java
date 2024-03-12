package org.example.mapper;

import org.example.dto.post.PostItemDTO;
import org.example.entities.PostEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "category.name",target = "category")
    @Mapping(source = "category.id",target = "category_id")
    PostItemDTO PostItemDTOByPost(PostEntity post);
}
