package com.kiriloff.max.imdb.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.Toast
import com.kiriloff.max.imdb.R
import com.kiriloff.max.imdb.domain.IMovie
import com.kiriloff.max.imdb.domain.IMovieInteractor
import com.kiriloff.max.imdb.view.details.DetailsActivity
import com.kiriloff.max.newsaggregator.di.component.DaggerDomainComponent
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainActivity : AppCompatActivity(), IMainContract.IView {

    private val mMainPresenter: IMainContract.IPresenter<IMainContract.IView> = MainPresenter()
    private var mMoviesAdapter: MoviesAdapter = MoviesAdapter()
    private var mCurrentViewState: IMainContract.ViewState = IMainContract.ViewState.DEFAULT

    private lateinit var mProgressBar: ContentLoadingProgressBar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToolbar: Toolbar
    private lateinit var mRootViewMainActivity: View

    init {
        mMainPresenter.attach(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindView()
        initToolbar()
        initRecyclerView()
        mMainPresenter.onCreate(savedInstanceState)
    }

    private fun bindView() {
        mRecyclerView = findViewById(R.id.recyclerView)
        mProgressBar = findViewById(R.id.progress)
        mToolbar = findViewById(R.id.toolbar)
        mRootViewMainActivity = findViewById(R.id.activity_main)
    }

    private fun initToolbar() {
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun initRecyclerView() {
        mRecyclerView.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            setHasFixedSize(true)
            adapter = mMoviesAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView?.layoutManager as GridLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItems = layoutManager.findFirstVisibleItemPosition()
                    if (visibleItemCount + firstVisibleItems + 5 >= totalItemCount) {
                        mMainPresenter.getNextData()
                    }
                }
            })
            mMoviesAdapter.setOnItemClickListener { movie, _ ->
                movie?.let {
                    mMainPresenter.onItemPressed(applicationContext, movie)
                }
            }
        }
    }

    override fun addItems(movies: Collection<IMovie>) {
        mMoviesAdapter.addItems(movies)
    }

    override fun setNewItems(movies: MutableList<IMovie>) {
        mMoviesAdapter.setNewItems(movies)
    }

    override fun clearItems() {
        mMoviesAdapter.cleanItems()
        mRecyclerView.removeAllViews()
        mRecyclerView.removeAllViewsInLayout()
    }

    override fun setViewState(state: IMainContract.ViewState) {
        if (mCurrentViewState == state) return

        when (state) {
            IMainContract.ViewState.LOADING -> {
                mRecyclerView.visibility = View.GONE
                mProgressBar.visibility = View.VISIBLE
                mCurrentViewState = IMainContract.ViewState.LOADING
            }
            IMainContract.ViewState.CONTENT -> {
                mProgressBar.visibility = View.GONE
                mRecyclerView.visibility = View.VISIBLE
                mCurrentViewState = IMainContract.ViewState.CONTENT
            }
            IMainContract.ViewState.EMPTY_CONTENT -> {
                mProgressBar.visibility = View.GONE
                mRecyclerView.visibility = View.GONE
                mCurrentViewState = IMainContract.ViewState.EMPTY_CONTENT
            }
            IMainContract.ViewState.SEARCH -> {
                mProgressBar.visibility = View.GONE
                mRecyclerView.visibility = View.VISIBLE
                mCurrentViewState = IMainContract.ViewState.SEARCH
            }
            else -> {
            }
        }
    }

    override fun getCurrentViewState(): IMainContract.ViewState {
        return mCurrentViewState
    }

    override fun showToast(message: String) {
        Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.maxWidth = Integer.MAX_VALUE

        Observable.create<String> { emitter ->
            searchView.setOnQueryTextListener(
                    object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(newText: String): Boolean {
                            emitter.onNext(newText)
                            return false
                        }
                    })
        }
                .toFlowable(BackpressureStrategy.LATEST)
                .subscribe { mMainPresenter.searchByQuery(it) }
        return true
    }

    override fun onStart() {
        super.onStart()
        mMainPresenter.onStart()
    }

    override fun onRestart() {
        super.onRestart()
        mMainPresenter.onRestart()
    }

    override fun onResume() {
        super.onResume()
        mMainPresenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMainPresenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        mMainPresenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainPresenter.onDestroy()
    }


    /*****************************************************************/
    /**                         Presenter                           **/
    /*****************************************************************/

    class MainPresenter : IMainContract.IPresenter<IMainContract.IView> {
        private var mView: IMainContract.IView? = null
        @Inject
        lateinit var mMovieInteractor: IMovieInteractor

        init {
            DaggerDomainComponent.builder().build().inject(this)
        }

        override fun attach(view: IMainContract.IView) {
            this.mView = view
        }

        override fun onCreate(bundle: Bundle?) {
            when (mView?.getCurrentViewState()) {
                IMainContract.ViewState.DEFAULT -> {
                    mMovieInteractor.getTopRatedMovies()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .doOnSubscribe { mView?.clearItems(); mView?.setViewState(IMainContract.ViewState.LOADING) }
                            .subscribe(
                                    { onNext ->
                                        mView?.addItems(onNext)
                                    },
                                    { mView?.showToast("") },
                                    { mView?.setViewState(IMainContract.ViewState.CONTENT) }
                            )
                }
                IMainContract.ViewState.CONTENT -> {
                }
                IMainContract.ViewState.EMPTY_CONTENT -> {
                }
                else -> {
                }
            }
        }

        override fun getNextData() {
            when (mView?.getCurrentViewState()) {
                IMainContract.ViewState.SEARCH -> {
                    mMovieInteractor.getMoviesByQueryNextPage()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    { onNext ->
                                        mView?.addItems(onNext)
                                    },
                                    { mView?.showToast("") },
                                    { })
                }
                IMainContract.ViewState.CONTENT -> {
                    mMovieInteractor.getTopRatedMoviesNextPage()
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(
                                    { onNext ->
                                        mView?.addItems(onNext)
                                    },
                                    { mView?.showToast("") },
                                    { })
                }
                else -> {
                }
            }
        }

        override fun searchByQuery(query: String) {
            when {
                query.isNotBlank() -> {
                    var queryResultList = mutableListOf<IMovie>()
                    mMovieInteractor.getMoviesByQuery(query)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.computation())
                            .toFlowable(BackpressureStrategy.LATEST)
                            .doOnSubscribe { mView?.setViewState(IMainContract.ViewState.LOADING); mView?.clearItems(); }
                            .subscribe(
                                    { onNext -> queryResultList = onNext },
                                    { mView?.showToast("") }
                            ) {
                                this.mView?.setNewItems(queryResultList)
                                if (queryResultList.isEmpty()) mView?.setViewState(IMainContract.ViewState.EMPTY_CONTENT)
                                else mView?.setViewState(IMainContract.ViewState.SEARCH)
                            }
                }
                query.isBlank() -> {
                    var queryResultList = mutableListOf<IMovie>()
                    when (mView?.getCurrentViewState()) {
                        IMainContract.ViewState.CONTENT -> {
                            /*NOP*/
                        }
                        IMainContract.ViewState.SEARCH -> {
                            mMovieInteractor.getTopRatedMovies()
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribeOn(Schedulers.io())
                                    .doOnSubscribe { mView?.setViewState(IMainContract.ViewState.LOADING); mView?.clearItems(); }
                                    .subscribe(
                                            { onNext -> queryResultList = onNext },
                                            { mView?.showToast("") }
                                    ) {
                                        this.mView?.setNewItems(queryResultList)
                                        if (queryResultList.isEmpty()) mView?.setViewState(IMainContract.ViewState.EMPTY_CONTENT)
                                        else mView?.setViewState(IMainContract.ViewState.CONTENT)
                                    }
                        }
                        else -> {
                        }
                    }
                }
            }
        }

        override fun onItemPressed(context: Context, movie: IMovie) {
            val intent = Intent(context, DetailsActivity::class.java)
            val bundle = Bundle().apply { putInt(DetailsActivity.EXTRA_MOVIE_ID, movie.id ?: 0) }
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        override fun detach() {
            mView = null
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
