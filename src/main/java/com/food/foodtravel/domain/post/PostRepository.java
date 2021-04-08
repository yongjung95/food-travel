package com.food.foodtravel.domain.post;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    List<Post> findAll();

    Page<Post> findByContentContainingOrTitleContaining(String content, String title, Pageable pageable);


}
