package com.kiriloff.max.imdb.view.main

import android.content.Context
import com.kiriloff.max.imdb.domain.IMovie

interface IMainContract {

    interface IView : IBaseView {

        fun addItems(movies: Collection<IMovie>)

        fun setNewItems(movies: MutableList<IMovie>)

        fun clearItems()

        fun setViewState(state: ViewState)

        fun getCurrentViewState(): ViewState

        fun showToast(message: String)

    }

    enum class ViewState {
        LOADING, EMPTY_CONTENT, CONTENT, DEFAULT, SEARCH
    }

    interface IPresenter<T> : IBasePresenterActivity<IView> {

        fun getNextData()

        fun searchByQuery(query: String)

        fun onItemPressed(context: Context, movie: IMovie)

    }
}