package org.example.services;

import org.example.dto.post.PostCreateDTO;
import org.example.dto.post.PostEditDTO;
import org.example.dto.post.PostItemDTO;
import org.example.dto.post.PostSearchResultDTO;

import java.util.List;

public interface PostService {
    PostItemDTO create(PostCreateDTO model);

    List<PostItemDTO> get();
    PostItemDTO edit(PostEditDTO model);
    PostItemDTO getById(Integer postId);

    PostSearchResultDTO searchPost(String name,int categoryId,String description,int page,int size);

    boolean delete(Integer postId);
}
