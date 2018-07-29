package com.kiriloff.max.imdb.domain

class MovieImpl(override val id: Int?) : IMovie {
    override var title: String? = null
    override var backdropPath: String? = null
    override var releaseDate: String? = null
    override var voteAverage: Double? = null
    override var overview: String? = null
}