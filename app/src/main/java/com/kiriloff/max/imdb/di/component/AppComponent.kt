package com.kiriloff.max.imdb.di.component

import com.kiriloff.max.newsaggregator.di.module.AppContextModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [(AppContextModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        fun setAppContextModule(appContextModule: AppContextModule): Builder
        fun build(): AppComponent
    }
}

