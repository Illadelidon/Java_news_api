package org.example.dto.post;

import lombok.Data;

import java.util.List;
@Data
public class PostEditDTO {
    private int id;
    private String name;
    private String description;
    private int category_id;
    public List<PostPhotoDTO> oldPhotos;
    public List<PostPhotoDTO> newPhotos;
}
