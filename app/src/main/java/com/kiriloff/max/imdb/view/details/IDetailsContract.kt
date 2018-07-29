package com.kiriloff.max.imdb.view.details

import com.kiriloff.max.imdb.domain.IMovieDetails
import com.kiriloff.max.imdb.view.main.IBasePresenterActivity
import com.kiriloff.max.imdb.view.main.IBaseView

interface IDetailsContract {

    interface IView : IBaseView {

        fun setViewState(state: ViewState)

        fun getCurrentViewState(): ViewState

        fun showMovieDetails(movie: IMovieDetails)

    }

    enum class ViewState {
        LOADING, ERROR, CONTENT, DEFAULT
    }

    interface IPresenter<T> : IBasePresenterActivity<IView>
}