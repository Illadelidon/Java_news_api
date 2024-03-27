package org.example.services;

import lombok.AllArgsConstructor;
import org.example.dto.post.*;
import org.example.entities.CategoryEntity;
import org.example.entities.PostEntity;
import org.example.entities.PostImageEntity;
import org.example.mapper.PostMapper;
import org.example.repositories.PostImageRepository;
import org.example.repositories.PostRepository;
import org.example.storage.FileSaveFormat;
import org.example.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.specification.PostEntitySpecifications.*;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final StorageService storageService;
    private final PostMapper postMapper;



    @Override
    public PostItemDTO create(PostCreateDTO model) {
        var p = new PostEntity();
        var cat = new CategoryEntity();
        cat.setId(model.getCategory_id());
        p.setName(model.getName());
        p.setDescription(model.getDescription());
        p.setDateCreated(LocalDateTime.now());
        p.setCategory(cat);
        p.setDelete(false);
        postRepository.save(p);
        int priority = 1;
        for (var img : model.getFiles()) {
            var file = storageService.SaveImage(img, FileSaveFormat.WEBP);
            PostImageEntity pi = new PostImageEntity();
            pi.setName(file);
            pi.setDateCreated(LocalDateTime.now());
            pi.setPriority(priority);
            pi.setDelete(false);
            pi.setPost(p);
            postImageRepository.save(pi);
            priority++;
        }
        return null;
    }

    @Override
    public List<PostItemDTO> get() {
        var list = new ArrayList<PostItemDTO>();
        var data = postRepository.findAll();
        for (var product : data) {
            PostItemDTO productItemDTO = new PostItemDTO();

            productItemDTO.setCategory(product.getCategory().getName());
            productItemDTO.setId(product.getId());
            productItemDTO.setName(product.getName());
            productItemDTO.setDescription(product.getDescription());

            var items = new ArrayList<String>();
            for (var img : product.getPostImages()) {
                items.add(img.getName());
            }
            productItemDTO.setFiles(items);
            list.add(productItemDTO);
        }
        return list;
    }

    @Override
    public PostItemDTO edit(PostEditDTO model) {
        var p = postRepository.findById(model.getId());
        if (p.isPresent()) {
            try {
                var post = p.get();
                var imagesDb = post.getPostImages();
                //Видаляємо фото, якщо потрібно
                for (var image : imagesDb) {
                    if (!isAnyImage(model.getOldPhotos(), image)) {
                        postImageRepository.delete(image);
                        storageService.deleteImage(image.getName());
                    }
                }
                //Оновляємо пріорітет фото у списку
                for(var old : model.getOldPhotos()) {
                    var imgUpdate = postImageRepository.findByName(old.getPhoto());
                    imgUpdate.setPriority(old.getPriority());
                    postImageRepository.save(imgUpdate);
                }
                var cat = new CategoryEntity();
                cat.setId(model.getCategory_id());
                post.setName(model.getName());
                post.setDescription(model.getDescription());
                post.setCategory(cat);
                postRepository.save(post);
                for (var img : model.getNewPhotos()) {
                    var file = storageService.SaveImageBase64(img.getPhoto(), FileSaveFormat.WEBP);
                    PostImageEntity pi = new PostImageEntity();
                    pi.setName(file);
                    pi.setDateCreated(LocalDateTime.now());
                    pi.setPriority(img.getPriority());
                    pi.setDelete(false);
                    pi.setPost(post);
                    postImageRepository.save(pi);
                }
            } catch (Exception ex) {
                System.out.println("Edit product is problem " + ex.getMessage());
            }
        }

        return null;
    }
    private boolean isAnyImage(List<PostPhotoDTO> list, PostImageEntity image) {
        boolean result = false;

        for (var item : list) {
            if (item.getPhoto().equals(image.getName()))
                return true;
        }
        return result;
    }
    @Override
    public PostItemDTO getById(Integer postId) {
        var entity = postRepository.findById(postId).orElse(null);
        if (entity == null) {
            return null;
        }

        var product = postMapper.PostItemDTOByPost(entity);
        product.setFiles(postImageRepository.findImageNamesByPost(entity));

        return product;
    }


    @Override
    public PostSearchResultDTO searchPost(String name, int categoryId, String description, int page, int size) {
        Page<PostEntity> result = postRepository
                .findAll(
                        findByCategoryId(categoryId).and(findByName(name)).and(findByDescription(description)),
                        PageRequest.of(page, size));

        List<PostItemDTO> products = result.getContent().stream()
                .map(product -> {
                    PostItemDTO productItemDTO = postMapper.PostItemDTOByPost(product);

                    // Retrieve image names for the current product
                    List<String> imageNames = postImageRepository.findImageNamesByPost(product);
                    productItemDTO.setFiles(imageNames);

                    return productItemDTO;
                })
                .collect(Collectors.toList());

        return new PostSearchResultDTO(products, (int) result.getTotalElements());
    }

    @Override
    public boolean delete(Integer postId) {
        var entity = postRepository.findById(postId).orElse(null);
        if (entity == null) {
            return false;
        }
        try {
            postRepository.deleteById(postId);
            return true;
        }
        catch (Exception exception) {
            return false;
        }
    }
}
