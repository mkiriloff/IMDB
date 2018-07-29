package com.kiriloff.max.imdb.domain

class MovieDetailsImpl : IMovieDetails {
    override var tagline: String? = null
    override var genres: Collection<IGenra?> = ArrayList<GenraImpl>()
    override var backdropPath: String? = null
    override var title: String? = null
    override var voteAverage: Double? = null
    override var productionCountries: Collection<IProductionContrite?> = ArrayList<IProductionContrite>()
    override var homepage: String? = null
    override var overview: String? = null
}

class GenraImpl : IGenra {
    override var id: Int? = null
    override var name: String? = null
}

class ProductionContriteImpl : IProductionContrite {
    override var iso31661: String? = null
    override var name: String? = null
}