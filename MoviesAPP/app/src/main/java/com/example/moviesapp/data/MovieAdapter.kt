package com.example.moviesapp.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.model.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(context: Context, movies: ArrayList<Movie>): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    private val movies1 = movies

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImageView : ImageView = itemView.findViewById(R.id.posterImageView)
        val titleTextView : TextView = itemView.findViewById(R.id.titleTextView)
        val yearTextView : TextView = itemView.findViewById(R.id.yearTextView)
        val movieCard: CardView = itemView.findViewById(R.id.movieCard)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currMovie = movies1[position]
        Picasso.get().load(currMovie.posterURL).fit().centerInside().into(holder.posterImageView)
        holder.titleTextView.setText(currMovie.title)
        holder.yearTextView.text = currMovie.year
        holder.movieCard.setOnClickListener {
            onClickListener1.onClick(position,currMovie)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val movieView: View = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return MovieViewHolder(movieView)
    }

    override fun getItemCount(): Int {
        return movies1.size
    }

    lateinit var onClickListener1: OnClickListener

    interface OnClickListener {
        fun onClick(position: Int, model: Movie)
    }
    fun setOnClickListener(onClickListenerParam: OnClickListener) {
        this.onClickListener1 = onClickListenerParam
    }

}