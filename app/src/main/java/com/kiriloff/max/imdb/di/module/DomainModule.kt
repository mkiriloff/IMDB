package com.kiriloff.max.imdb.di.module

import com.kiriloff.max.imdb.domain.IMovieInteractor
import com.kiriloff.max.imdb.domain.IRepoMovie
import com.kiriloff.max.imdb.domain.MovieDetailsImpl
import com.kiriloff.max.imdb.domain.MovieInteractor
import com.kiriloff.max.imdb.dto.MovieImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DomainModule {

    @Provides
    @Singleton
    fun providesIMovieInteractor(repoMovie: IRepoMovie<MovieImpl, MovieDetailsImpl>): IMovieInteractor {
        return MovieInteractor(repoMovie)

    }
}