package org.example.mapper;

import org.example.dto.category.CategoryCreateDTO;
import org.example.dto.category.CategoryEditDTO;
import org.example.dto.category.CategoryItemDTO;
import org.example.dto.common.SelectItemDTO;
import org.example.entities.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
    @Mapping(source = "creationTime", target = "dateCreated", dateFormat = "dd.MM.yyyy HH:mm:ss")
    CategoryItemDTO categoryItemDTO(CategoryEntity category);
    List<CategoryItemDTO> categoryItemDTOList(List<CategoryEntity> categories);

    CategoryEntity categoryEntityByCategoryCreateDTO(CategoryCreateDTO category);
    SelectItemDTO selectItemDTO(CategoryEntity category);
    CategoryEntity categoryEditDto(CategoryEditDTO dto);
}
