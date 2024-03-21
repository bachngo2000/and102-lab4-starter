package com.codepath.articlesearch

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val ARTICLE_EXTRA = "ARTICLE_EXTRA"
private const val TAG = "ArticleAdapter"

class ArticleAdapter(private val context: Context, private val articles: List<Article>) : RecyclerView.Adapter<ArticleAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Get the individual article from our list of articles, and set the UI for it using our helper method
        val article = articles[position]
        holder.bind(article)
    }

    override fun getItemCount() = articles.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        private val mediaImageView = itemView.findViewById<ImageView>(R.id.mediaImage)
        private val titleTextView = itemView.findViewById<TextView>(R.id.mediaTitle)
        private val abstractTextView = itemView.findViewById<TextView>(R.id.mediaAbstract)

        init {
            itemView.setOnClickListener(this)
        }

        // helper method to help set up the onBindViewHolder method
        fun bind(article: Article) {
            titleTextView.text = article.headline?.main
            abstractTextView.text = article.abstract

            Glide.with(context)
                .load(article.mediaImageUrl)
                .into(mediaImageView)
        }

        override fun onClick(v: View?) {
            // The first thing we will want to do in passing along the article information is to get the selected article from the array
            val article = articles[absoluteAdapterPosition]

            // Navigate to Details screen and pass selected article
            // Next, we want to send the app to the details screen, along with information about the article that was clicked on. We can do this in 3 steps:
            //  1. We can create an Intent, using the current context, and the Class that we are navigating to.
            val intent = Intent(context, DetailActivity::class.java)
            //  2. We can use .putExtra to pass along the article, with the first argument being the key for the data, and the next being article itself.
            intent.putExtra(ARTICLE_EXTRA, article)
            //  3. We can then call startActivity with the current context, using the intent as its param
            // Implicit Intents are requests to perform an action based on a desired action and target data
            context.startActivity(intent)
        }
    }
}