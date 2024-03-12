package org.example.specification;

import org.example.entities.PostEntity;
import org.springframework.data.jpa.domain.Specification;

public class PostEntitySpecifications {
    public static Specification<PostEntity> findByCategoryId(int categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == 0) {
                return criteriaBuilder.notEqual(root.get("category").get("id"), 0);
            } else {
                return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
            }
        };
    }

    public static Specification<PostEntity> findByName(String name) {
        return (root, query, cb) -> {
            return cb.like(root.get("name"), "%"+name+"%");
        };
    }

    public static Specification<PostEntity> findByDescription(String description) {
        return (root, query, cb) -> {
            return cb.like(root.get("description"), "%"+description+"%");
        };
    }
}
