package com.kiriloff.max.imdb.data.api.pojo

import com.google.gson.annotations.SerializedName
import java.util.*

data class FullMovie(
        @SerializedName("adult")
        var adult: Boolean = false,
        @SerializedName("backdrop_path")
        var backdropPath: String? = null,
        @SerializedName("belongs_to_collection")
        var belongsToCollection: Any? = null,
        @SerializedName("budget")
        var budget: Int = 0,
        @SerializedName("genres")
        var genres: List<Genre>? = null,
        @SerializedName("homepage")
        var homepage: String? = null,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("imdb_id")
        var imdbId: String? = null,
        @SerializedName("original_language")
        var originalLanguage: String? = null,
        @SerializedName("original_title")
        var originalTitle: String? = null,
        @SerializedName("overview")
        var overview: String? = null,
        @SerializedName("popularity")
        var popularity: Double = 0.toDouble(),
        @SerializedName("poster_path")
        var posterPath: String? = null,
        @SerializedName("production_companies")
        var productionCompanies: List<ProductionCompany>? = null,
        @SerializedName("production_countries")
        var productionCountries: List<ProductionCountry>? = null,
        @SerializedName("release_date")
        var releaseDate: String? = null,
        @SerializedName("revenue")
        var revenue: Int = 0,
        @SerializedName("runtime")
        var runtime: Int = 0,
        @SerializedName("spoken_languages")
        var spokenLanguages: List<SpokenLanguage>? = null,
        @SerializedName("status")
        var status: String? = null,
        @SerializedName("tagline")
        var tagline: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("video")
        var video: Boolean = false,
        @SerializedName("vote_average")
        var voteAverage: Double = 0.toDouble(),
        @SerializedName("vote_count")
        var voteCount: Int = 0)

data class Genre(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("name")
        var name: String? = null)

data class ProductionCompany(
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("id")
        var id: Int = 0)

data class ProductionCountry(
        @SerializedName("iso_3166_1")
        var iso31661: String? = null,
        @SerializedName("name")
        var name: String? = null)

data class SpokenLanguage(
        @SerializedName("iso_639_1")
        var iso6391: String? = null,
        @SerializedName("name")
        var name: String? = null)

data class Movie(
        @SerializedName("poster_path")
        var posterPath: String? = null,
        @SerializedName("adult")
        var adult: Boolean = false,
        @SerializedName("overview")
        var overview: String? = null,
        @SerializedName("release_date")
        var releaseDate: String? = null,
        @SerializedName("genre_ids")
        var genreIds: List<Int> = ArrayList(),
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("original_title")
        var originalTitle: String? = null,
        @SerializedName("original_language")
        var originalLanguage: String? = null,
        @SerializedName("title")
        var title: String? = null,
        @SerializedName("backdrop_path")
        var backdropPath: String? = null,
        @SerializedName("popularity")
        var popularity: Double? = null,
        @SerializedName("vote_count")
        var voteCount: Int? = null,
        @SerializedName("video")
        var video: Boolean? = null,
        @SerializedName("vote_average")
        var voteAverage: Double? = null)

data class MoviesResponse(
        @SerializedName("page")
        var page: Int = 0,
        @SerializedName("results")
        var results: List<Movie>? = null,
        @SerializedName("total_results")
        var totalResults: Int = 0,
        @SerializedName("total_pages")
        var totalPages: Int = 0)
