package com.moviecatalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieCatalogItem {
    private String movieId;
    private String movieName;
    private double averageRating;
}
