package com.kiriloff.max.newsaggregator.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppContextModule(private val mContext: Context) {

    @Provides
    @Singleton
    fun providesContext(): Context {
        return mContext
    }
}