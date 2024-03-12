package org.example.dto.category;

import lombok.Data;

import java.util.List;
@Data
public class CategorySearchResultDTO {
    //список знайдених категорій
    private List<CategoryItemDTO> list;
    //кількість усіх записів
    private int totalCount;
}
