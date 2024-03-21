package com.codepath.articlesearch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.articlesearch.databinding.ActivityMainBinding
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import kotlinx.serialization.json.Json
import okhttp3.Headers
import org.json.JSONException

fun createJson() = Json {
    isLenient = true
    ignoreUnknownKeys = true
    useAlternativeNames = false
}

private const val TAG = "MainActivity/"
private var SEARCH_API_KEY = BuildConfig.API_KEY
// Since we will be using the Article Search API, this is the endpoint that we'll be using
// It references the API key with ${SEARCH_API_KEY}
private var ARTICLE_SEARCH_URL =
    "https://api.nytimes.com/svc/search/v2/articlesearch.json?api-key=${SEARCH_API_KEY}"

class MainActivity : AppCompatActivity() {

    // property for our list of articles that we will be fetching from the server
    private val articles = mutableListOf<Article>()

    private lateinit var articlesRecyclerView: RecyclerView
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        articlesRecyclerView = findViewById(R.id.articles)

        // set up the ArticleAdapter will the list of articles
        val articleAdapter = ArticleAdapter(this, articles)
        articlesRecyclerView.adapter = articleAdapter

        articlesRecyclerView.layoutManager = LinearLayoutManager(this).also {
            val dividerItemDecoration = DividerItemDecoration(this, it.orientation)
            articlesRecyclerView.addItemDecoration(dividerItemDecoration)
        }

        // Using the AsyncHTTPClient to request the data from the search API.
        val client = AsyncHttpClient()
        client.get(ARTICLE_SEARCH_URL, object : JsonHttpResponseHandler() {

            // In case the request fails, we'll be logging the error
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "Failed to fetch articles: $statusCode")
            }

            // If it's successful, we'll need to parse through the JSON data that we get back to get the articles.
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                Log.i(TAG, "Successfully fetched articles: $json")
                try {
                    // Create the parsedJSON
                    // The Serialization library helps us to take the information and convert it into Kotlin objects we can work with.
                    // Decoding is the part where serialization occurs and converts the JSON data into models
                    val parsedJson = createJson().decodeFromString(
                        SearchNewsResponse.serializer(),
                        json.jsonObject.toString()
                    )

                    // Since the response object was the second outermost layer, we need to dig into our model using dot notation to get the articles:
                    //  - The response will have a docs array, which will be our list of articles.
                    //  - Going through the docs array, we'll take each article and add it to our articles mutable list.
                    // Save the articles
                    parsedJson.response?.docs?.let { list ->
                        articles.addAll(list)
                    }

                    // reload the screen
                    // When called, notifyDataSetChanged() makes the RecyclerView reload and update the data it's displaying
                    articleAdapter.notifyDataSetChanged()


                } catch (e: JSONException) {
                    Log.e(TAG, "Exception: $e")
                }
            }

        })

    }
}