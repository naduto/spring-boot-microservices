package com.example.trendingmoviesservice.service;
import com.example.trendingmoviesservice.dto.Movie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MovieInfoClient {

    private final RestTemplate restTemplate;

    @Value("${movie.info.service.url:http://localhost:8082}")
    private String movieInfoServiceUrl;

    public MovieInfoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "movies", key = "#movieId", unless = "#result == null")
    public Movie getMovieInfo(String movieId) {
        String url = movieInfoServiceUrl + "/movies/" + movieId;
        return restTemplate.getForObject(url, Movie.class);
    }
}