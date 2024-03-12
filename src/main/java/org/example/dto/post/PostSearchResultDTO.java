package org.example.dto.post;

import lombok.Data;

import java.util.List;

@Data
public class PostSearchResultDTO {
    private List<PostItemDTO> list;
    private int totalCount;

    public PostSearchResultDTO(List<PostItemDTO> list,int totalCount){
        this.list = list;
        this.totalCount = totalCount;
    }

}
