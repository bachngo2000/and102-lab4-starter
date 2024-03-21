package com.codepath.articlesearch

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

private const val TAG = "DetailActivity"

class DetailActivity : AppCompatActivity() {
    private lateinit var mediaImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var bylineTextView: TextView
    private lateinit var abstractTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // Find the views for the screen
        mediaImageView = findViewById(R.id.mediaImage)
        titleTextView = findViewById(R.id.mediaTitle)
        bylineTextView = findViewById(R.id.mediaByline)
        abstractTextView = findViewById(R.id.mediaAbstract)

        // Get the extra from the Intent
        // Next, we will get the information from the Intent that was passed to the DetailActivity.
        // We can get this using .getSerializableExtra() and passing in the name of the extra we need:
        val article = intent.getSerializableExtra(ARTICLE_EXTRA) as Article

        // Once we have the article object, we can then access the properties on it using our dot notation.
        // Set the title, byline, and abstract information from the article
        titleTextView.text = article.headline?.main
        bylineTextView.text = article.byline?.original
        abstractTextView.text = article.abstract

        // Load the media image
        // we'll use Glide to set our image, using the mediaImageUrl we have from the article object
        Glide.with(this)
            .load(article.mediaImageUrl)
            .into(mediaImageView)
    }
}