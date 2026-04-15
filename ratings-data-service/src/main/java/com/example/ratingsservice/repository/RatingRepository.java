package com.example.ratingsservice.repository;

import com.example.ratingsservice.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

    public interface RatingRepository extends JpaRepository<Rating, Integer> {
    List<Rating> findByUserId(String userId);
    @Query("SELECT r.movieId AS movieId, AVG(r.rating) AS averageRating " +
            "FROM Rating r " +
            "GROUP BY r.movieId " +
            "ORDER BY AVG(r.rating) DESC")
    List<MovieAverageProjection> findTopNMoviesByAverageRating(Pageable pageable);
}
