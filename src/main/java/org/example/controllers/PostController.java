package org.example.controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.dto.post.PostCreateDTO;
import org.example.dto.post.PostEditDTO;
import org.example.dto.post.PostItemDTO;
import org.example.dto.post.PostSearchResultDTO;
import org.example.services.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/posts")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostItemDTO>> index() {

        return new ResponseEntity<>(postService.get(), HttpStatus.OK);
    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostItemDTO> create(@Valid @ModelAttribute PostCreateDTO model) {
        var result = postService.create(model);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping
    public ResponseEntity<PostItemDTO> edit(@RequestBody PostEditDTO model) {
        var result = postService.edit(model);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<PostItemDTO> getById(@PathVariable int productId) {
        var result = postService.getById(productId);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<PostSearchResultDTO> searchProducts(
            @RequestParam(defaultValue = "") String name,
            @RequestParam(defaultValue = "0") int categoryId,
            @RequestParam(defaultValue = "") String description,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        PostSearchResultDTO searchResult = postService.searchPost(name, categoryId,
                description, page, size);
        return new ResponseEntity<>(searchResult, HttpStatus.OK);
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(@PathVariable int postId) {
        var success = postService.delete(postId);
        if (!success) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
