package com.example.moviesapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.moviesapp.R
import com.example.moviesapp.data.MovieAdapter
import com.example.moviesapp.model.Movie
import com.example.moviesapp.model.MovieDetailed
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONException

class MovieActivity : AppCompatActivity() {
    private lateinit var requestQueue: RequestQueue
    lateinit var textViewActors:TextView
    lateinit var textViewPlot:TextView

    lateinit var movieDetails:MovieDetailed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        textViewActors = findViewById<TextView>(R.id.textViewActors)
        textViewPlot = findViewById<TextView>(R.id.textViewPlot)
        val imdbID = intent.getStringExtra("IMDB_Id").toString()

        requestQueue = Volley.newRequestQueue(this)

        textViewActors.visibility=android.view.View.GONE
        textViewPlot.visibility=android.view.View.GONE
        main(imdbID)


    }
    fun main(imdbID: String) = runBlocking {
        val task = async {getDetails(imdbID)}
        task.await()


    }
    suspend fun getDetails(imdbID: String): Boolean {
        val url = "https://www.omdbapi.com/?i=$imdbID&apikey=f8e2947f"
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET,

            url,null,
            {
                try {


                    val item = it
                    val actors = item.getString("Actors")
                    val plot = item.getString("Plot")
                    movieDetails = MovieDetailed(imdbID,actors, plot)

                    Log.d("MyFilm", actors)
                    var progressBar = findViewById<ProgressBar>(R.id.progressBar)
                    progressBar.visibility = android.view.View.GONE
                    textViewActors.visibility=android.view.View.VISIBLE
                    textViewPlot.visibility=android.view.View.VISIBLE
                    Log.d("MyFIlm", movieDetails.plot)
                    textViewActors.text = movieDetails.actors
                    textViewPlot.text = movieDetails.plot


                } catch ( e: JSONException) {
                    e.printStackTrace()
                }
            }, {
                it.printStackTrace()
            })

        requestQueue.add(jsonRequest)
        return true
    }
}