package com.example.trendingmoviesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieScore {
    String movieName;
    String movieId;
    Double AvgRating;

}
