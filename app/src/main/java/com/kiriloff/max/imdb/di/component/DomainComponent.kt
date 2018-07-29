package com.kiriloff.max.newsaggregator.di.component

import com.kiriloff.max.imdb.di.module.DomainModule
import com.kiriloff.max.imdb.view.details.DetailsActivity
import com.kiriloff.max.imdb.view.main.MainActivity
import com.kiriloff.max.newsaggregator.di.module.DataModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(DataModule::class), DomainModule::class])
interface DomainComponent {

    @Component.Builder
    interface Builder {
        fun setDomainModule(domainModule: DomainModule): DomainComponent.Builder
        fun setDataModule(dataModule: DataModule): DomainComponent.Builder
        fun build(): DomainComponent
    }

    fun inject(mainPresenter: MainActivity.MainPresenter)

    fun inject(detailsPresenter: DetailsActivity.DetailsPresenter)

}