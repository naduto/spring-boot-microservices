package com.moviecatalogservice.services;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.example.grpc.trending.*;

import java.util.List;
@Service
public class MovieCatalogClient {

    @GrpcClient("trending-movies-service")
    private TrendingServiceGrpc.TrendingServiceBlockingStub trendingStub;

    public List<MovieRatingInfo> getTopTrendingMovies(int limit) {
        try {

            TrendingRequest request = TrendingRequest.newBuilder()
                    .setLimit(limit)
                    .build();

            TrendingResponse response = trendingStub.getTopTrending(request);

            return response.getMoviesList();

        } catch (Exception e) {
            System.err.println("Error calling trending-service: " + e.getMessage());
            return List.of();
        }
    }
}

