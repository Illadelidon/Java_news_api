package org.example.dto.post;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PostItemDTO {
    private int id;
    private String name;
    private String description;
    private String category;
    private int category_id;
    private List<String> files = new ArrayList<>();
}
