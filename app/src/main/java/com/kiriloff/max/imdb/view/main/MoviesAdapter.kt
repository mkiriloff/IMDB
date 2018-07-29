package com.kiriloff.max.imdb.view.main

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kiriloff.max.imdb.R
import com.kiriloff.max.imdb.domain.IMovie
import com.kiriloff.max.imdb.util.GlideApp

class MoviesAdapter() : RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {
    private val IMAGE_KEY = "https://image.tmdb.org/t/p/w500"
    private var mMovies = mutableListOf<IMovie>()
    private var mOnItemClickListener = fun(_: IMovie?, _: Int) {}
    private val block = Any()

    fun setOnItemClickListener(onItemClickListener: ((movie: IMovie?, positiom: Int) -> Unit)?) {
        onItemClickListener?.let { mOnItemClickListener = it }
    }

    fun setNewItems(movies: MutableList<IMovie>) {
        synchronized(block) {
            this.mMovies = movies
            notifyDataSetChanged()
        }
    }

    fun addItems(movies: Collection<IMovie>) {
        synchronized(block) {
            this.mMovies.addAll(movies)
            notifyItemRangeInserted(this.mMovies.size, this.mMovies.size + movies.size)
        }
    }

    fun cleanItems() {
        synchronized(block) {
            if (mMovies.isNotEmpty()) {
                notifyItemRangeRemoved(0, this.mMovies.size)
            }
            this.mMovies.clear()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MoviesAdapter.ViewHolder, position: Int) {
        mMovies[position]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return mMovies.size
    }

    inner class ViewHolder(IMovie: View) : RecyclerView.ViewHolder(IMovie), View.OnClickListener {

        private var mBackdropPath: ImageView = itemView.findViewById(R.id.iv_cv_backdrop_path) as ImageView
        private var mTitle: TextView = itemView.findViewById(R.id.tv_cv_title) as TextView
        private var mTagLine: TextView = itemView.findViewById(R.id.tv_cv_tagline) as TextView
        private var mRating: TextView = itemView.findViewById(R.id.tv_cv_rating) as TextView
        private var mYear: TextView = itemView.findViewById(R.id.tv_cv_data) as TextView

        init {
            val rootCardView = itemView.findViewById(R.id.card_view_post) as CardView
            rootCardView.setOnClickListener(this)
        }

        fun bind(movie: IMovie) {
            if (null in setOf(movie.title, movie.backdropPath, movie.overview, movie.releaseDate, movie.voteAverage, movie.id)) return

            mTitle.text = movie.title
            mTagLine.text = movie.overview
            mRating.text = movie.voteAverage.toString()
            mYear.text = movie.releaseDate

            GlideApp.with(mBackdropPath.context)
                    .load(IMAGE_KEY + movie.backdropPath)
                    .centerCrop()
                    .into(mBackdropPath);
        }

        override fun onClick(v: View) {
            if (mMovies.size >= adapterPosition) {
                mMovies[adapterPosition]?.let { mOnItemClickListener(it, adapterPosition) }
            }
        }
    }
}