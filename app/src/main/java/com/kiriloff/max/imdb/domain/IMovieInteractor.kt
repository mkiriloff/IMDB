package com.kiriloff.max.imdb.domain

import io.reactivex.Observable

interface IMovieInteractor {

    fun getTopRatedMovies(): Observable<MutableList<IMovie>>

    fun getMoviesByQuery(query: String): Observable<MutableList<IMovie>>

    fun getTopRatedMoviesNextPage(): Observable<MutableList<IMovie>>

    fun getMoviesByQueryNextPage(): Observable<MutableList<IMovie>>

    fun getMovieDetails(id: Int): Observable<IMovieDetails>
}