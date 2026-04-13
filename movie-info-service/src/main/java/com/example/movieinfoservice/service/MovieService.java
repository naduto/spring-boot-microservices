package com.example.movieinfoservice.service;

import com.example.movieinfoservice.entity.Movie;
import com.example.movieinfoservice.dto.MovieSummary;
import com.example.movieinfoservice.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

//aaaaaaaaaaaaaaaa
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Value("${api.key}")
    private String apiKey;

    private RestTemplate restTemplate;

    public MovieService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Movie getMovieInfo(String movieId) {
        java.util.Optional<Movie> cachedMovie = movieRepository.findById(movieId);
        if (cachedMovie.isPresent()) {
            return cachedMovie.get();
        }
        final String url = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=" + apiKey;
        MovieSummary movieSummary = restTemplate.getForObject(url, MovieSummary.class);

        if (movieSummary == null || movieSummary.getTitle() == null) {
            throw new RuntimeException("Movie not found");
        }
        Movie movie = new Movie(movieId, movieSummary.getTitle(), movieSummary.getOverview());

        movieRepository.save(movie);
        return movie;

    }
}