package com.food.foodtravel.domain.post;

import com.food.foodtravel.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {

    List<Favorite> findAllByUser(User user);

    Optional<Favorite> findByUserAndPost(User user, Post post);
}
