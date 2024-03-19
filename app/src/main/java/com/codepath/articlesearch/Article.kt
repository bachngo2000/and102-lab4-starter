package com.codepath.articlesearch

// // JSON Parsing Step 0: Imports
// These imports will allow us to use the Kotlin Serialization library
import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// JSON Parsing Step 1: Response
/*
    "response"   // nested JSON
*/
// To model the first part of the JSON response, we will create a data class.
// The annotation (with the @ symbol) marks it as Serializable, and will be the initial response we get from the server.
// It will map to the top level key "response" that we see in the JSON.
@Keep
@Serializable
data class SearchNewsResponse(
    @SerialName("response")
    val response: BaseResponse?
)


// JSON Parsing Step 2: Docs
/*
    "response"   // nested JSON
        ├─ "docs"  // list of JSON objects
*/
// Next we will represent "docs" with another data class
// This will map to the base response, which will include the array of documents (articles). Each array element will be an individual article.
@Keep
@Serializable
data class BaseResponse(
    @SerialName("docs")
    val docs: List<Article>?
)

// JSON Parsing Step 3: Article
/*
"response"            // nested JSON
  ├─ "docs"           // list of JSON objects
    ├─ Article[0]
      ├─ "abstract"   // String
      ├─ "byline"     // nested JSON
      ├─ "headline"   // nested JSON
      ├─ "multimedia" // list of JSON objects
*/
/*
   We've made it down to the Article layer! Now, we will need to identify which pieces of information we need from the JSON
   response for each of the pieces that will be displayed on screen. We will need the following:
   - The title for the Article (contained in the "headline" key)
   - The Article's media image url (contained in the "multimedia" key)
   - The Article's abstract (description of the article)
   - The byline information (author)
*/
/*
    We can access one key right away -- "abstract".

    Unfortunately, for the next couple of pieces, we will have to dig in further into the JSON:
       - The byline information (author) is contained in the "byline" object
       - The title for the Article is contained under the "headline" key
       - The Article's media image url is an array of multimedia objects
*/
@Keep
@Serializable
data class Article(
    @SerialName("abstract")
    val abstract: String?,

    @SerialName("byline")
    val byline: Byline?,

    @SerialName("headline")
    val headline: HeadLine?,

    @SerialName("multimedia")
    val multimedia: List<MultiMedia>?,

) : java.io.Serializable {
    // code to handle Multimedia
    // This will use the first media in the array, if it's available (by checking using firstOrNull),
    // and parse through the MultiMedia to get the url for the image. If it is not available, it will set an empty string for the mediaImageUrl.
    val mediaImageUrl = "https://www.nytimes.com/${multimedia?.firstOrNull { it.url != null }?.url ?: ""}"
}

// JSON Parsing Step 4a: Headline
/*
"response"
  ├─ "docs"
    ├─ Article[0]
      ├─ "abstract"   // String
      ├─ "byline"     // nested JSON
        ├─ "original" // String
      ├─ "headline"   // nested JSON
        ├─ "???"
      ├─ "multimedia" // list of JSON objects
 */
@Keep
@Serializable
data class HeadLine(
    @SerialName("main")
    val main: String
) : java.io.Serializable

// JSON Parsing Step 4b: Byline
@Keep
@Serializable
data class Byline(
    @SerialName("original")
    val original: String? = null
) : java.io.Serializable

// JSON Parsing Step 5: Multimedia
/*
"response"
  ├─ "docs"
    ├─ Article[0]
      ├─ "abstract"   // String
      ├─ "byline"
        ├─ "original" // String
      ├─ "headline"
        ├─ "main"     // String
      ├─ "multimedia" // list of JSON objects
*/
@Keep
@Serializable
data class MultiMedia(
    @SerialName("url")
    val url: String?
) : java.io.Serializable