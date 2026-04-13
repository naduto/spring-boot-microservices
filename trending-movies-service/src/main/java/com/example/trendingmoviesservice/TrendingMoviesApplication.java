package com.example.trendingmoviesservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class TrendingMoviesApplication {

    private final int TIMEOUT = 3000;   // 3 seconds


    public static void main(String[] args) {
        SpringApplication.run(TrendingMoviesApplication.class, args);
    }

}
