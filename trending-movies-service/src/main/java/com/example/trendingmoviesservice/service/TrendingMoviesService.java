package com.example.trendingmoviesservice.service;
import com.example.trendingmoviesservice.dto.Movie;
import com.example.trendingmoviesservice.dto.MovieScore;
import com.example.trendingmoviesservice.dto.Rating;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrendingMoviesService {
    private final RatingsClient ratingsClient;
    private final MovieInfoClient movieInfoClient;

    public TrendingMoviesService(RatingsClient ratingsClient, MovieInfoClient movieInfoClient) {
        this.ratingsClient = ratingsClient;
        this.movieInfoClient = movieInfoClient;
    }

    @Cacheable(value = "topRatedMovies", key = "#topN", unless = "#result == null || #result.isEmpty()")
    public List<MovieScore> getTopRatedMovies(int topN) {
        // Get all ratings
        List<Rating> allRatings = ratingsClient.getAllRatings();

        // Group by movieId and calculate average rating
        Map<String, Double> movieAverages = allRatings.stream()
                .collect(Collectors.groupingBy(
                        Rating::getMovieId,
                        Collectors.averagingInt(Rating::getRating)
                ));

        // Sort by average rating and get top N
        List<Map.Entry<String, Double>> topMovies = movieAverages.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(topN)
                .collect(Collectors.toList());

        // Fetch movie details and create MovieScore objects
        return topMovies.stream()
                .map(entry -> {
                    String movieId = entry.getKey();
                    Double avgRating = entry.getValue();

                    try {
                        Movie movie = movieInfoClient.getMovieInfo(movieId);
                        String movieName = movie != null ? movie.getName() : "Unknown";
                        return new MovieScore(movieId, movieName, avgRating);
                    } catch (Exception e) {
                        // If movie info not found, still include with movieId
                        return new MovieScore(movieId, "Unknown", avgRating);
                    }
                })
                .collect(Collectors.toList());
    }
}
