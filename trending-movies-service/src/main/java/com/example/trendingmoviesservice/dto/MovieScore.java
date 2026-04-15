package com.example.trendingmoviesservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MovieScore {
    String movieName;
    String movieId;
    Double AvgRating;

    public MovieScore(String movieId, String movieName, Double avgRating) {
        this.movieId = movieId;
        this.movieName = movieName;
        this.AvgRating = avgRating;
    }

}
