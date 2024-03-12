package org.example.dto.post;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PostCreateDTO {
    private String name;
    private String description;
    private int category_id;
    private List<MultipartFile> files;
}
