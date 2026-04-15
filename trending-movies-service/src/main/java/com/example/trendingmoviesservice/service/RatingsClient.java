package com.example.trendingmoviesservice.service;

import com.example.trendingmoviesservice.dto.MovieAverage;
import com.example.trendingmoviesservice.dto.Rating;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class RatingsClient {

    private final RestTemplate restTemplate;

    @Value("${ratings.service.url:http://localhost:8083}")
    private String ratingsServiceUrl;

    public RatingsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

//    @Cacheable(value = "allRatings", unless = "#result == null || #result.isEmpty()")
    public List<Rating> getAllRatings() {
        String url = ratingsServiceUrl + "/ratings/all";
        Rating[] ratings = restTemplate.getForObject(url, Rating[].class);
//        System.out.println(ratings.length);
        return ratings != null ? Arrays.asList(ratings) : List.of();
    }
    public List<MovieAverage> getTopRatedMovies(int topN) {
        String url = ratingsServiceUrl + "/ratings/top/" + topN;
        MovieAverage[] results = restTemplate.getForObject(url, MovieAverage[].class);
        return results != null ? Arrays.asList(results) : List.of();
    }
}