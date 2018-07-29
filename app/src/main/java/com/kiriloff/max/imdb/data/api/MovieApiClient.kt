package com.kiriloff.max.imdb.data.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class MovieApiClient {
    private val BASE_URL = "http://api.themoviedb.org/3/"
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        return retrofit ?: invoke()
    }

    private fun invoke(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}