package com.moviecatalogservice.services;

import com.example.grpc.trending.MovieRatingInfo;
import com.moviecatalogservice.dto.MovieCatalogItem;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class catalogService {

    private final MovieCatalogClient movieCatalogClient;

    public catalogService(MovieCatalogClient movieCatalogClient) {
        this.movieCatalogClient = movieCatalogClient;
    }

    @Cacheable(value = "topMovies", key = "#limit")
    public List<MovieCatalogItem> getTrending(int limit) {
        // This only runs on cache miss
        // On cache hit, Spring returns from Redis directly
        List<MovieRatingInfo> grpcMovies = movieCatalogClient.getTopTrendingMovies(limit);
        return grpcMovies.stream()
                .map(m -> new MovieCatalogItem(
                        m.getMovieId(),
                        m.getMovieName(),
                        m.getAverageRating()))
                .collect(Collectors.toList());
    }
}