package com.example.ratingsservice.Config;

import com.example.ratingsservice.entity.Rating;
import com.example.ratingsservice.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Configuration
public class DataLoader {

    @Value("${api.key}")
    private String API_KEY;

    @Bean
    CommandLineRunner loadData(RatingRepository repository) {
        return args -> {

            RestTemplate restTemplate = new RestTemplate();
            Random random = new Random();

            // 1. Clear existing data
            repository.deleteAll();
            System.out.println("Cleared ratings table");

            // 2. Fetch movie ids from TMDB
            List<Integer> movieIds = new ArrayList<>();

            for (int page = 1; page <= 20; page++) {

                String url = "https://api.themoviedb.org/3/movie/popular?api_key="
                        + API_KEY + "&page=" + page;

                Map response = restTemplate.getForObject(url, Map.class);

                List<Map<String, Object>> results =
                        (List<Map<String, Object>>) response.get("results");

                for (Map<String, Object> movie : results) {
                    movieIds.add((Integer) movie.get("id"));
                }

                Thread.sleep(200);
            }

            List<Rating> ratings = new ArrayList<>();

            int total = 100000;

            for (int i = 0; i < total; i++) {

                String movieId = String.valueOf(
                        movieIds.get(random.nextInt(movieIds.size()))
                );

                String userId = "user" + random.nextInt(1000);

                int rating = random.nextInt(5) + 1;

                ratings.add(new Rating(movieId, rating, userId));

                if (ratings.size() == 1000) {
                    repository.saveAll(ratings);
                    ratings.clear();
                    System.out.println("Inserted batch...");
                }
            }

            repository.saveAll(ratings);

            System.out.println("Data generation complete");
        };
    }
}