package com.kiriloff.max.newsaggregator.di.module

import com.kiriloff.max.imdb.data.RepoMovie
import com.kiriloff.max.imdb.data.api.IMovieApiInterface
import com.kiriloff.max.imdb.data.api.MovieApiClient
import com.kiriloff.max.imdb.domain.IRepoMovie
import com.kiriloff.max.imdb.domain.MovieDetailsImpl
import com.kiriloff.max.imdb.domain.MovieImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataModule {

    @Provides
    @Singleton
    fun providesIMovieApiInterface(): IMovieApiInterface {
        return MovieApiClient().getClient().create(IMovieApiInterface::class.java)
    }

    @Provides
    @Singleton
    fun providesIRepoMovie(mMovieApiInterface: IMovieApiInterface): IRepoMovie<MovieImpl, MovieDetailsImpl> {
        return RepoMovie(mMovieApiInterface)
    }
}