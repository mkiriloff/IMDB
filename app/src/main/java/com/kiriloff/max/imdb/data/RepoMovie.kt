package com.kiriloff.max.imdb.data

import com.kiriloff.max.imdb.data.api.IMovieApiInterface
import com.kiriloff.max.imdb.domain.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class RepoMovie(private val mMovieApiInterface: IMovieApiInterface) : IRepoMovie<MovieImpl, MovieDetailsImpl> {

    private val API_KEY = "72b56103e43843412a992a8d64bf96e9"
    private var lastQuery: String = ""
    private var lastPage: Int = 0
    private var totalPages: Int = 0
    private val isValidData = fun(it: IMovie): Boolean = null !in setOf(it.title, it.backdropPath, it.overview, it.releaseDate, it.voteAverage, it.id)

    override fun getTopRatedMovies(): Observable<MutableList<MovieImpl>> {
        return mMovieApiInterface.getTopRatedMovies(API_KEY)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    lastPage = it.page
                    totalPages = it.totalPages
                    Observable.fromIterable(it.results)
                }
                .map {
                    MovieImpl(it.id).apply {
                        title = it.title
                        backdropPath = it.backdropPath
                        releaseDate = it.releaseDate
                        voteAverage = it.voteAverage
                        overview = it.overview
                    }
                }
                .filter { isValidData(it) }
                .toList()
                .toObservable()
    }

    override fun getTopRatedMoviesNextPage(): Observable<MutableList<MovieImpl>> {
        lastPage = lastPage.inc()
        return mMovieApiInterface.getTopRatedMoviesNextPage(API_KEY, lastPage)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    lastPage = it.page
                    totalPages = it.totalPages
                    Observable.fromIterable(it.results)
                }
                .map {
                    MovieImpl(it.id).apply {
                        title = it.title
                        backdropPath = it.backdropPath
                        releaseDate = it.releaseDate
                        voteAverage = it.voteAverage
                        overview = it.overview
                    }
                }
                .filter { isValidData(it) }
                .toList()
                .toObservable()
    }

    override fun getMoviesByQuery(query: String): Observable<MutableList<MovieImpl>> {
        this.lastQuery = query
        return mMovieApiInterface.getMoviesByQuery(API_KEY, query)
                .subscribeOn(Schedulers.computation())
                .flatMap {
                    lastPage = it.page
                    totalPages = it.totalPages
                    Observable.fromIterable(it.results)
                }
                .map {
                    MovieImpl(it.id).apply {
                        title = it.title
                        backdropPath = it.backdropPath
                        releaseDate = it.releaseDate
                        voteAverage = it.voteAverage
                        overview = it.overview
                    }
                }
                .filter { isValidData(it) }
                .toList()
                .toObservable()
    }

    override fun getMoviesByQueryNextPage(): Observable<MutableList<MovieImpl>> {
        lastPage = lastPage.inc()
        if (lastPage > totalPages) return Observable.empty()
        return mMovieApiInterface.getMoviesByQueryNextPage(API_KEY, lastQuery, lastPage)
                .subscribeOn(Schedulers.io())
                .flatMap {
                    lastPage = it.page
                    totalPages = it.totalPages
                    Observable.fromIterable(it.results)
                }
                .map {
                    MovieImpl(it.id).apply {
                        title = it.title
                        backdropPath = it.backdropPath
                        releaseDate = it.releaseDate
                        voteAverage = it.voteAverage
                        overview = it.overview
                    }
                }
                .filter { isValidData(it) }
                .toList()
                .toObservable()
    }

    override fun getMovieDetails(id: Int): Observable<MutableList<MovieDetailsImpl>> {
        return mMovieApiInterface.getMovieDetails(id, API_KEY)
                .subscribeOn(Schedulers.io())
                .map {
                    MovieDetailsImpl().apply {
                        title = it.title
                        backdropPath = it.backdropPath
                        voteAverage = it.voteAverage
                        homepage = it.homepage
                        overview = it.overview
                        tagline = it.tagline
                        productionCountries = it.productionCountries
                                ?.map { ProductionContriteImpl().apply { name = it.name } }
                                ?.toList() ?: emptyList()
                        genres = it.genres
                                ?.map { GenraImpl().apply { name = it.name } }
                                ?.toList() ?: emptyList()
                    }
                }
                .toList()
                .toObservable()
    }
}