package com.kiriloff.max.imdb.data.api

import com.kiriloff.max.imdb.data.api.pojo.FullMovie
import com.kiriloff.max.imdb.data.api.pojo.MoviesResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMovieApiInterface {

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") apiKey: String): Observable<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMoviesNextPage(@Query("api_key") apiKey: String, @Query("page") page: Int): Observable<MoviesResponse>

    @GET("search/movie")
    fun getMoviesByQuery(@Query("api_key") apiKey: String, @Query("query") query: String): Observable<MoviesResponse>

    @GET("search/movie")
    fun getMoviesByQueryNextPage(@Query("api_key") apiKey: String, @Query("query") query: String, @Query("page") page: Int): Observable<MoviesResponse>

    @GET("movie/{id}")
    fun getMovieDetails(@Path("id") id: Int, @Query("api_key") apiKey: String): Observable<FullMovie>
}