package com.example.movieinfoservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.movieinfoservice.entity.Movie;

public interface MovieRepository extends MongoRepository<Movie, String> {

}