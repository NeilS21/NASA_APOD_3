package com.codepath.nasaapod

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit

class MainActivity : AppCompatActivity() {
    private lateinit var currentDate: LocalDate
    private val apodStartDate = LocalDate.of(1995, 6, 16)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private lateinit var apodRecyclerView: RecyclerView
    private lateinit var apodAdapter: ApodAdapter
    private val apodItems = mutableListOf<ApodItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        com.jakewharton.threetenabp.AndroidThreeTen.init(this)
        currentDate = LocalDate.now()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        val imageIV = findViewById<ImageView>(R.id.imageView)
//        val titleTV = findViewById<TextView>(R.id.textView)
//        val explanationTV: TextView = findViewById(R.id.textView2)
//        val prevButton: Button = findViewById(R.id.prevButton)
//        val nextButton: Button = findViewById(R.id.nextButton)
//        val randomButton: Button = findViewById(R.id.randButton)

//        fetchAPOD()

//        randomButton.setOnClickListener {
//            val randomDate = getRandomDate()
//            fetchAPOD(randomDate)
//        }
//        prevButton.setOnClickListener {
//            val prevDate = currentDate.minusDays(1)
//            if(!prevDate.isBefore(apodStartDate)) {
//                fetchAPOD(prevDate.format(formatter))
//            }
//        }
//        nextButton.setOnClickListener {
//            val nextDate = currentDate.plusDays(1)
//            val todayDate = LocalDate.now()
//            if(!nextDate.isAfter(todayDate)) {
//                fetchAPOD(nextDate.format(formatter))
//            }
//        }

        apodRecyclerView = findViewById(R.id.rvContainer)
        apodAdapter = ApodAdapter(apodItems)
        apodRecyclerView.layoutManager = LinearLayoutManager(this)
        apodRecyclerView.adapter = apodAdapter

        fetchAPODList()

    }

    private fun fetchAPODList() {
        val API_Key = "F0GwdQZnNAjxBu8IdM7hrBzP6zxIniOe6J9is2U8"
        val url = "https://api.nasa.gov/planetary/apod?api_key=$API_Key&count=20"

        val client = AsyncHttpClient()
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                val jsonArray = json.jsonArray
                apodItems.clear() // Clear any previous items
                for (i in 0 until jsonArray.length()) {
                    val obj: JSONObject = jsonArray.getJSONObject(i)
                    if (obj.getString("media_type") == "image") {
                        val item = ApodItem(
                            title = obj.getString("title"),
                            explanation = obj.getString("explanation"),
                            url = obj.getString("url"),
                            date = obj.getString("date"),
                            media_type = obj.getString("media_type")
                        )
                        apodItems.add(item)
                    }
                }
                apodAdapter.notifyDataSetChanged()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Toast.makeText(this@MainActivity, "Failed to fetch APOD list", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
//    private fun fetchAPODList() {
//        val API_Key = "F0GwdQZnNAjxBu8IdM7hrBzP6zxIniOe6J9is2U8"
//        var url = "https://api.nasa.gov/planetary/apod?api_key=$API_Key&count=20"
//
//
//        val client = AsyncHttpClient()
//
//        client[url, object : JsonHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
//                val response = json.jsonObject
//                val title = response.getString("title")
//                val apodDate = response.getString("date")
//                currentDate = LocalDate.parse(apodDate)
//                val explanation = response.getString("explanation")
//                val imageUrl = response.getString("url")
//                val mediaType = response.getString("media_type")
//
//                if (mediaType != "image") {
//                    Toast.makeText(this@MainActivity, "Not an image. Try another date.", Toast.LENGTH_SHORT).show()
//                    return
//                }
//
//                val titleTV: TextView = findViewById(R.id.textView)
//                val explanationTV: TextView = findViewById(R.id.textView2)
//                val imageIV: ImageView = findViewById(R.id.imageView)
//
//                titleTV.text = "$title - ($apodDate)"
//                explanationTV.text = explanation
//
//                Glide.with(this@MainActivity)
//                    .load(imageUrl)
//                    .fitCenter()
//                    .into(imageIV)
//            }
//
//            override fun onFailure(statusCode: Int,
//                                   headers: Headers?,
//                                   errorResponse: String,
//                                   throwable: Throwable?) {
//                Toast.makeText(this@MainActivity, "Failed to fetch APOD data", Toast.LENGTH_SHORT).show()
//            }
//        }]
//    }

//    private fun getRandomDate(): String {
//        val today = LocalDate.now()
//        val daysBetween = ChronoUnit.DAYS.between(apodStartDate, today)
//        val randomOffset = (0..daysBetween).random()
//        return apodStartDate.plusDays(randomOffset).format(formatter)
//    }

//    private fun fetchAPOD(date: String? = null) {
//        val API_Key = "F0GwdQZnNAjxBu8IdM7hrBzP6zxIniOe6J9is2U8"
//
////        var url = "https://api.nasa.gov/planetary/apod?api_key=$API_Key"
//        var url = "https://api.nasa.gov/planetary/apod?api_key=$API_Key&count=20"
//        if(date != null) {
//            url += "&date=$date"
//        }
//        val client = AsyncHttpClient()
//
//        client[url, object : JsonHttpResponseHandler() {
//            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
//                val response = json.jsonObject
//                val title = response.getString("title")
//                val apodDate = response.getString("date")
//                currentDate = LocalDate.parse(apodDate)
//                val explanation = response.getString("explanation")
//                val imageUrl = response.getString("url")
//                val mediaType = response.getString("media_type")
//
//                if (mediaType != "image") {
//                    Toast.makeText(this@MainActivity, "Not an image. Try another date.", Toast.LENGTH_SHORT).show()
//                    return
//                }
//
//                val titleTV: TextView = findViewById(R.id.textView)
//                val explanationTV: TextView = findViewById(R.id.textView2)
//                val imageIV: ImageView = findViewById(R.id.imageView)
//
//                titleTV.text = "$title - ($apodDate)"
//                explanationTV.text = explanation
//
//                Glide.with(this@MainActivity)
//                    .load(imageUrl)
//                    .fitCenter()
//                    .into(imageIV)
//            }
//
//            override fun onFailure(statusCode: Int,
//                                   headers: Headers?,
//                                   errorResponse: String,
//                                   throwable: Throwable?) {
//                Toast.makeText(this@MainActivity, "Failed to fetch APOD data", Toast.LENGTH_SHORT).show()
//            }
//        }]
//    }
//}