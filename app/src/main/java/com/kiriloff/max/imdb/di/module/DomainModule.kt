package com.kiriloff.max.imdb.di.module

import com.kiriloff.max.imdb.domain.*
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