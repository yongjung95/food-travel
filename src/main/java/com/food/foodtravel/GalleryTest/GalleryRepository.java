package com.food.foodtravel.GalleryTest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryRepository extends JpaRepository<GalleryEntity, Long> {
    @Override
    List<GalleryEntity> findAll();

    List<GalleryEntity> findByTitle(String title);
}