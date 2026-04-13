package com.moviecatalogservice.Controller;

import com.example.grpc.trending.MovieRatingInfo;
import com.moviecatalogservice.dto.MovieCatalogItem;
import com.moviecatalogservice.models.CatalogItem;
import com.moviecatalogservice.services.MovieCatalogClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private static final Logger logger = LoggerFactory.getLogger(CatalogController.class);

    private final MovieCatalogClient movieCatalogClient;

    public CatalogController(MovieCatalogClient movieCatalogClient) {
        this.movieCatalogClient = movieCatalogClient;
    }

    @GetMapping("/trending/{limit}")
    public List<MovieCatalogItem> getTrending(@PathVariable int limit) {

            List<MovieRatingInfo> grpcMovies = movieCatalogClient.getTopTrendingMovies(limit);

            return grpcMovies.stream()
                    .map(m -> new MovieCatalogItem(m.getMovieId(), m.getMovieName(), m.getAverageRating()))
                    .collect(Collectors.toList());

    }
}