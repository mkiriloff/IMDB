package com.kiriloff.max.imdb.domain

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class MovieInteractor(private val repoMovie: IRepoMovie<out IMovie, out IMovieDetails>) : IMovieInteractor {

    override fun getTopRatedMovies(): Observable<MutableList<IMovie>> {
        return repoMovie.getTopRatedMovies()
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .toList()
                .toObservable()
    }

    override fun getMoviesByQuery(query: String): Observable<MutableList<IMovie>> {
        return repoMovie.getMoviesByQuery(query)
                .subscribeOn(Schedulers.computation())
                .flatMap { Observable.fromIterable(it) }
                .toList()
                .toObservable()
    }

    override fun getTopRatedMoviesNextPage(): Observable<MutableList<IMovie>> {
        return repoMovie.getTopRatedMoviesNextPage()
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .toList()
                .toObservable()
    }

    override fun getMoviesByQueryNextPage(): Observable<MutableList<IMovie>> {
        return repoMovie.getMoviesByQueryNextPage()
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
                .toList()
                .toObservable()
    }

    override fun getMovieDetails(id: Int): Observable<IMovieDetails> {
        return repoMovie.getMovieDetails(id)
                .subscribeOn(Schedulers.io())
                .flatMap { Observable.fromIterable(it) }
    }
}