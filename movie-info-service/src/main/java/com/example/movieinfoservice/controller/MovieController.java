package com.example.movieinfoservice.controller;

import com.example.movieinfoservice.entity.Movie;
import com.example.movieinfoservice.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @RestController
    @RequestMapping("/movies")
    public class MovieResource {

        @Autowired
        private MovieService movieService;

        @GetMapping("/{movieId}")
        public ResponseEntity<?> getMovie(@PathVariable String movieId) {
            try {
                Movie movie = movieService.getMovieInfo(movieId);
                return ResponseEntity.ok(movie);

            } catch (RuntimeException e) {
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());

            } catch (Exception e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Something went wrong");
            }
        }
    }
}
