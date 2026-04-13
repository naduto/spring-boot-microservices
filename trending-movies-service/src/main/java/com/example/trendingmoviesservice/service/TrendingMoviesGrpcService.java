package com.example.trendingmoviesservice.service;

import com.example.trendingmoviesservice.dto.MovieScore;
import com.example.grpc.trending.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class TrendingMoviesGrpcService extends TrendingServiceGrpc.TrendingServiceImplBase {

    private final TrendingMoviesService trendingMoviesService;

    public TrendingMoviesGrpcService(TrendingMoviesService trendingMoviesService) {
        this.trendingMoviesService = trendingMoviesService;
    }

    @Override
    public void getTopTrending(TrendingRequest request,
                               StreamObserver<TrendingResponse> responseObserver) {

        try {
            int topN = request.getLimit();

            if (topN <= 0) {
                topN = 10;
            }

            List<MovieScore> topMovies = trendingMoviesService.getTopRatedMovies(topN);

            List<MovieRatingInfo> grpcMovies =
                    topMovies.stream()
                            .map(movie -> MovieRatingInfo.newBuilder()
                                    .setMovieId(String.valueOf(movie.getMovieId()))
                                    .setMovieName(movie.getMovieName())
                                    .setAverageRating(movie.getAvgRating())
                                    .build())
                            .collect(Collectors.toList());

            TrendingResponse response = TrendingResponse.newBuilder()
                    .addAllMovies(grpcMovies)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}