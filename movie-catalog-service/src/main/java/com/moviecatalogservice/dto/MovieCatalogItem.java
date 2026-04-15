package com.moviecatalogservice.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MovieCatalogItem implements Serializable {
    private String movieId;
    private String movieName;
    private double averageRating;
}
