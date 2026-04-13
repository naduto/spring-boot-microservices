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

//    @Cacheable(value = "topRatedMovies", key = "#topN", unless = "#result == null || #result.isEmpty()")
//    public List<MovieScore> getTopRatedMovies(int topN) {
//        System.out.println("Hello");
//        // Get all ratings
//        List<Rating> allRatings = ratingsClient.getAllRatings();
//
//        // Group by movieId and calculate average rating
//        Map<String, Double> movieAverages = allRatings.stream()
//                .collect(Collectors.groupingBy(
//                        Rating::getMovieId,
//                        Collectors.averagingInt(Rating::getRating)
//                ));
//
//        // Sort by average rating and get top N
//        List<Map.Entry<String, Double>> topMovies = movieAverages.entrySet().stream()
//                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
//                .limit(topN)
//                .collect(Collectors.toList());
//
//        // Fetch movie details and create MovieScore objects
//        return topMovies.stream()
//                .map(entry -> {
//                    String movieId = entry.getKey();
//                    Double avgRating = entry.getValue();
//
//                    try {
//                        Movie movie = movieInfoClient.getMovieInfo(movieId);
//                        String movieName = movie != null ? movie.getName() : "Unknown";
//                        return new MovieScore(movieId, movieName, avgRating);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        // If movie info not found, still include with movieId
//                        return new MovieScore(movieId, "Unknown", avgRating);
//                    }
//                })
//                .collect(Collectors.toList());
//    }
    public List<MovieScore> getTopRatedMovies(int topN) {
        try {
            System.out.println("--- DIAGNOSTIC START ---");

            // 1. Check Ratings Client
            System.out.println("Step 1: Calling ratingsClient...");
            List<Rating> allRatings = ratingsClient.getAllRatings();

            if (allRatings == null) {
                System.err.println("FAILURE: ratingsClient returned null list");
                return List.of();
            }
            System.out.println("Step 1 Success: Received " + allRatings.size() + " ratings");

            // 2. Grouping and Averaging
            System.out.println("Step 2: Grouping and calculating averages...");
            Map<String, Double> movieAverages = allRatings.stream()
                    .filter(r -> r != null && r.getMovieId() != null) // Ensure no null entries
                    .collect(Collectors.groupingBy(
                            Rating::getMovieId,
                            Collectors.averagingInt(r -> {
                                // This is a common crash point: unboxing a null Integer
                                return (r.getRating() != 0) ? r.getRating() : 0;
                            })
                    ));
            System.out.println("Step 2 Success: Grouped into " + movieAverages.size() + " unique movies");

            // 3. Sorting
            List<Map.Entry<String, Double>> topMovies = movieAverages.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(topN)
                    .collect(Collectors.toList());

            // 4. Movie Info Fetching
            System.out.println("Step 3: Fetching movie details for top " + topMovies.size() + " items");
            return topMovies.stream()
                    .map(entry -> {
                        String movieId = entry.getKey();
                        Double avgRating = entry.getValue();
                        try {
                            Movie movie = movieInfoClient.getMovieInfo(movieId);
                            // Protobuf builders CRASH on null strings, so we use .orElse("Unknown")
                            String name = (movie != null && movie.getName() != null) ? movie.getName() : "Unknown";
                            return new MovieScore(movieId, name, avgRating);
                        } catch (Exception e) {
                            System.err.println("Warning: Could not fetch info for movie " + movieId + ": " + e.getMessage());
                            return new MovieScore(movieId, "Unknown", avgRating);
                        }
                    })
                    .collect(Collectors.toList());

        } catch (Throwable t) {
            // This will print the EXACT stack trace to your terminal
            System.err.println("!!! CRITICAL FAILURE IN SERVICE !!!");
            t.printStackTrace();
            throw t;
        } finally {
            System.out.println("--- DIAGNOSTIC END ---");
        }
    }
}
