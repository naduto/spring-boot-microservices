package com.example.trendingmoviesservice.service;
import com.example.trendingmoviesservice.dto.Movie;
import com.example.trendingmoviesservice.dto.MovieAverage;
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
    public List<MovieScore> getTopRatedMovies(int topN) {
        List<MovieAverage> topRated = ratingsClient.getTopRatedMovies(topN);

        return topRated.stream()
                .map(entry -> {
                    String movieId = entry.getMovieId();
                    Double avgRating = entry.getAverageRating();
                    try {
                        Movie movie = movieInfoClient.getMovieInfo(movieId);
                        String name = (movie != null && movie.getName() != null)
                                ? movie.getName() : "Unknown";
                        return new MovieScore(movieId, name, avgRating);
                    } catch (Exception e) {
                        System.err.println("Could not fetch info for movie " + movieId + ": " + e.getMessage());
                        return new MovieScore(movieId, "Unknown", avgRating);
                    }
                })
                .collect(Collectors.toList());
    }
}
