package com.kiriloff.max.imdb.domain

interface IMovieDetails {
    var title: String?
    var backdropPath: String?
    var voteAverage: Double?
    var tagline: String?
    var genres: Collection<IGenra?>
    var productionCountries: Collection<IProductionContrite?>
    var homepage: String?
    var overview: String?
}


interface IProductionContrite {
    var iso31661: String?
    var name: String?
}


interface IGenra {
    var id: Int?
    var name: String?
}