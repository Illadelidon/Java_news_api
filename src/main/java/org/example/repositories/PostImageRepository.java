package org.example.repositories;

import org.example.entities.PostEntity;
import org.example.entities.PostImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PostImageRepository extends JpaRepository<PostImageEntity,Integer> {
    PostImageEntity findByName(String name);

    @Query("SELECT pi.name FROM PostImageEntity pi WHERE pi.post = :post ORDER BY pi.priority")
    List<String> findImageNamesByPost(@Param("post")PostEntity post);
}
