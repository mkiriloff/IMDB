package com.kiriloff.max.imdb.domain

interface IMovie {
    val id: Int?
    var title: String?
    var backdropPath: String?
    var releaseDate: String?
    var voteAverage: Double?
    var overview: String?
}