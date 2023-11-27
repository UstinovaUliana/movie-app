package com.example.moviesapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.moviesapp.R
import com.example.moviesapp.data.MovieAdapter
import com.example.moviesapp.model.Movie
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var movies: ArrayList<Movie>
    private lateinit var requestQueue: RequestQueue
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var btnFindFilms: Button
    private lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        Log.d("mytag", "recycler1")
        recyclerView.hasFixedSize() //для улучшения производительности
        recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

        Log.d("mytag", "recycler2")

        movies = ArrayList()
        requestQueue = Volley.newRequestQueue(this)


        Log.d("mytag", "rk=" + requestQueue.toString())

        editText = findViewById(R.id.editTextTextFilmName)
        btnFindFilms = findViewById(R.id.buttonFindFilms)
        btnFindFilms.setOnClickListener {
            movies.clear()
            val filmName = editText.text.toString().lowercase().trim()
            val url = "https://www.omdbapi.com/?s=$filmName&apikey=f8e2947f"
            requestQueue.add(getMovies(url))
        }



        // var movies = generateMovies()
        //movieAdapter = MovieAdapter(this@MainActivity, movies)
        //recyclerView.adapter = MovieAdapter(this,movies)
    }


    private fun getMovies(url: String) : JsonObjectRequest {
        val jsonRequest = JsonObjectRequest(Request.Method.GET,
            url,null,
            {
                try {
                    val jsonArray = it.getJSONArray("Search")
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val title = item.getString("Title")
                        val year = item.getString("Year")
                        val posterURL = item.getString("Poster")
                        val IMDB_Id = item.getString("imdbID")
                        val movie = Movie(title, posterURL, year,IMDB_Id)
                        movies.add(movie)
                    }
                        //TODO: Отсортировать фильмы по году
                    movieAdapter = MovieAdapter(this@MainActivity, movies)
                    movieAdapter.setOnClickListener(object: MovieAdapter.OnClickListener {
                        override fun onClick(position: Int, model: Movie) {
                            val intent = Intent(this@MainActivity, MovieActivity::class.java)
                            intent.putExtra("IMDB_Id", model.IMDB_id)
                            startActivity(intent)
                        }
                    })
                    recyclerView.adapter = movieAdapter

                    Log.d("mytag", recyclerView.adapter.toString())
                } catch ( e: JSONException) {
                    e.printStackTrace()
                }
        }, {
            it.printStackTrace()
        })

        Log.d("mytag", "jsr1=" + jsonRequest.toString())
        return jsonRequest
    }


}