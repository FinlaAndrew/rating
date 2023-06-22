package com.rating.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rating.model.Rating;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {


    boolean existsByItemIdAndUserId(Long id, Long id1);
    List<Rating> findByItemId(Long itemId);

    List<String> findFeedbackByCourseId(Long courseId);

    List<Rating> findByUserId(Long userId);

    Rating findByItemIdAndUserId(Long itemId, Long userId);



}

