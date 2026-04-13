package com.example.trendingmoviesservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rating {
    private int id;
    private String movieId;
    private int rating;
    private String userId;
}