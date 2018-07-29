package com.kiriloff.max.imdb.view.details

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kiriloff.max.imdb.R
import com.kiriloff.max.imdb.domain.IMovieDetails
import com.kiriloff.max.imdb.domain.IMovieInteractor
import com.kiriloff.max.imdb.util.GlideApp
import com.kiriloff.max.newsaggregator.di.component.DaggerDomainComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class DetailsActivity : AppCompatActivity(), IDetailsContract.IView {

    private val IMAGE_KEY = "https://image.tmdb.org/t/p/w500"

    companion object {
        val EXTRA_MOVIE_ID = DetailsActivity::class.java.name + "_movie_extra_id"
    }

    private var currentViewState: IDetailsContract.ViewState = IDetailsContract.ViewState.DEFAULT
    private val mDetailsPresenter: IDetailsContract.IPresenter<IDetailsContract.IView> = DetailsPresenter()

    private lateinit var mBackdrop: ImageView
    private lateinit var mTitle: TextView
    private lateinit var mTagline: TextView
    private lateinit var mOverview: TextView
    private lateinit var mGenres: TextView
    private lateinit var mRating: TextView
    private lateinit var mErrorMessage: TextView
    private lateinit var mProductionCountries: TextView
    private lateinit var mContentLoadingProgressBar: ContentLoadingProgressBar
    private lateinit var mAppBarLayout: AppBarLayout
    private lateinit var mNestedScrollView: NestedScrollView

    init {
        mDetailsPresenter.attach(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        mAppBarLayout = findViewById(R.id.app_bar_layout)
        mNestedScrollView = findViewById(R.id.scroll)
        mBackdrop = findViewById(R.id.iv_vl_backdrop_path)
        mTitle = findViewById(R.id.tv_vl_title)
        mRating = findViewById(R.id.tv_vl_rating)
        mTagline = findViewById(R.id.tv_vl_tagline)
        mOverview = findViewById(R.id.tv_vl_overview)
        mGenres = findViewById(R.id.tv_vl_genres_data)
        mProductionCountries = findViewById(R.id.tv_vl_production_countries_data)
        mContentLoadingProgressBar = findViewById(R.id.progress)
        mErrorMessage = findViewById(R.id.tv_error)

        val bundle = intent?.extras

        if (bundle != null) {
            mDetailsPresenter.onCreate(bundle)
        } else {
            mDetailsPresenter.onCreate(savedInstanceState)
        }
    }

    override fun setViewState(state: IDetailsContract.ViewState) {
        if (currentViewState == state) return

        when (state) {
            IDetailsContract.ViewState.LOADING -> {
                mErrorMessage.visibility = View.GONE
                mAppBarLayout.visibility = View.GONE
                mNestedScrollView.visibility = View.GONE
                mContentLoadingProgressBar.show()
                mContentLoadingProgressBar.visibility = View.VISIBLE
                currentViewState = IDetailsContract.ViewState.LOADING
            }
            IDetailsContract.ViewState.CONTENT -> {
                mErrorMessage.visibility = View.GONE
                mContentLoadingProgressBar.hide()
                mContentLoadingProgressBar.visibility = View.GONE
                mAppBarLayout.visibility = View.VISIBLE
                mNestedScrollView.visibility = View.VISIBLE
                currentViewState = IDetailsContract.ViewState.CONTENT
            }
            IDetailsContract.ViewState.ERROR -> {
                mContentLoadingProgressBar.hide()
                mContentLoadingProgressBar.visibility = View.GONE
                mAppBarLayout.visibility = View.GONE
                mNestedScrollView.visibility = View.GONE
                mErrorMessage.visibility = View.VISIBLE
                currentViewState = IDetailsContract.ViewState.ERROR
            }
            else -> {
            }
        }
    }

    override fun getCurrentViewState(): IDetailsContract.ViewState {
        return currentViewState
    }

    override fun showMovieDetails(movie: IMovieDetails) {
        GlideApp.with(this)
                .load(IMAGE_KEY + movie.backdropPath)
                .centerInside()
                .into(mBackdrop)

        val genresTextBuilder = StringBuilder()
        val productionCountriesTextBuilder = StringBuilder()

        movie.genres
                .filterNotNull()
                .forEach {
                    genresTextBuilder.append(it.name)
                    genresTextBuilder.append("\n")
                }
        movie.productionCountries
                .filterNotNull()
                .forEach {
                    productionCountriesTextBuilder.append(it.name)
                    genresTextBuilder.append("\n")
                }

        mTitle.text = movie.title
        mRating.text = movie.voteAverage.toString()
        mTagline.text = movie.tagline
        mOverview.text = movie.overview
        mGenres.text = genresTextBuilder.toString()
        mProductionCountries.text = productionCountriesTextBuilder.toString()
    }

    override fun onStart() {
        super.onStart()
        mDetailsPresenter.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        mDetailsPresenter.onRestart()
    }

    override fun onResume() {
        super.onResume()
        mDetailsPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mDetailsPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        mDetailsPresenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mDetailsPresenter.onDestroy()
    }


    /*****************************************************************/
    /**                         Presenter                           **/
    /*****************************************************************/

    class DetailsPresenter : IDetailsContract.IPresenter<IDetailsContract.IView> {
        val BUNDLE_MOVIE_ID = DetailsActivity::class.java.name + "_MovieBundle_Id"

        private var mView: IDetailsContract.IView? = null

        @Inject
        lateinit var mMovieInteractor: IMovieInteractor

        init {
            DaggerDomainComponent.builder().build().inject(this)
        }

        override fun attach(view: IDetailsContract.IView) {
            this.mView = view
        }

        override fun detach() {
            this.mView = null
        }

        override fun onCreate(bundle: Bundle?) {
            val movieId = bundle?.getInt(EXTRA_MOVIE_ID, 0)

            if (movieId != null && movieId != 0) {
                mView?.setViewState(IDetailsContract.ViewState.LOADING)

                mMovieInteractor.getMovieDetails(movieId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                                { onNext ->
                                    mView?.showMovieDetails(onNext)
                                    mView?.setViewState(IDetailsContract.ViewState.CONTENT)
                                },
                                { mView?.setViewState(IDetailsContract.ViewState.ERROR) },
                                { mView?.setViewState(IDetailsContract.ViewState.CONTENT) }
                        )
            } else {
                mView?.setViewState(IDetailsContract.ViewState.ERROR)
            }
        }

        override fun onStart() {

        }

        override fun onRestart() {

        }

        override fun onResume() {

        }

        override fun onPause() {

        }

        override fun onStop() {

        }

        override fun onDestroy() {

        }
    }
}