package org.example.repositories;

import org.example.entities.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity,Integer>, JpaSpecificationExecutor<PostEntity> {
    @Query("SELECT p FROM PostEntity p WHERE LOWER(p.name) LIKE LOWER(:keywordName)"+
            "AND LOWER(p.category.name) LIKE LOWER(:keywordCategory)"+
            "AND LOWER(p.description) LIKE LOWER(:keywordDescription)")
    Page<PostEntity> searchPost(
        @Param("keywordName") String keywordName,
        @Param("keywordCategory") String keywordCategory,
        @Param("keywordDescription") String keywordDescription,
        Pageable pageable
    );
}
