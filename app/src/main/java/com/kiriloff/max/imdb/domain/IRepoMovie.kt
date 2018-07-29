package com.kiriloff.max.imdb.domain

import io.reactivex.Observable

interface IRepoMovie<M, D> {

    fun getTopRatedMovies(): Observable<MutableList<M>>

    fun getMoviesByQuery(query: String): Observable<MutableList<M>>

    fun getMoviesByQueryNextPage(): Observable<MutableList<M>>

    fun getTopRatedMoviesNextPage(): Observable<MutableList<M>>

    fun getMovieDetails(id: Int): Observable<MutableList<D>>

}