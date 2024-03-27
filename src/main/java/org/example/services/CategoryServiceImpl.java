package org.example.services;

import lombok.AllArgsConstructor;
import org.example.dto.category.CategoryCreateDTO;
import org.example.dto.category.CategoryEditDTO;
import org.example.dto.category.CategoryItemDTO;
import org.example.dto.category.CategorySearchResultDTO;
import org.example.dto.common.SelectItemDTO;
import org.example.entities.CategoryEntity;
import org.example.mapper.CategoryMapper;
import org.example.repositories.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    @Override
    public Page<CategoryItemDTO> getAllCategories(Pageable pageable) {
        Page<CategoryEntity> categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::categoryItemDTO);
    }

    @Override
    public boolean delete(Integer categoryId) {
        var entity = categoryRepository.findById(categoryId).orElse(null);
        if (entity == null) {
            return false;
        }
        try {
            categoryRepository.deleteById(categoryId);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }

    @Override
    public CategoryItemDTO getById(Integer categoryId) {
        var entity = categoryRepository.findById(categoryId).orElse(null);
        if (entity == null) {
            return null;
        }
        return categoryMapper.categoryItemDTO(entity);
    }

    @Override
    public CategoryItemDTO create(CategoryCreateDTO model) {
        var entity = categoryMapper.categoryEntityByCategoryCreateDTO(model);
        entity.setCreationTime(LocalDateTime.now());
        categoryRepository.save(entity);
        return categoryMapper.categoryItemDTO(entity);
    }

    @Override
    public CategoryItemDTO edit(CategoryEditDTO model)
    {
        var old = categoryRepository.findById(model.getId()).orElse(null);
        if (old == null) {
            return null;
        }
        var entity = categoryMapper.categoryEditDto(model);

        categoryRepository.save(entity);
        return categoryMapper.categoryItemDTO(entity);
    }

    @Override
    public CategorySearchResultDTO searchCategories(String keyword, int page, int size) {
        var result = categoryRepository.searchByNameContainingIgnoreCase(keyword, PageRequest.of(page, size));
        var searchResult = new CategorySearchResultDTO();
        searchResult.setList(categoryMapper.categoryItemDTOList(result.getContent()));
        searchResult.setTotalCount((int)result.getTotalElements());
        return searchResult;
    }

    @Override
    public List<SelectItemDTO> getNames() {
        var products = categoryRepository.findAll().stream()
                .map(category -> {
                    var dto = categoryMapper.selectItemDTO(category);
                    return dto;
                })
                .collect(Collectors.toList());
        return products;
    }
}
