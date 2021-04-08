package com.food.foodtravel.domain.image;

import com.food.foodtravel.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findAllByPost(Post post);

}
