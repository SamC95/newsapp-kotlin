package com.example.newsapp.activities

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.data.APIRequests
import com.example.newsapp.adapters.ArticlesAdapter
import com.example.newsapp.BuildConfig
import com.example.newsapp.data.CountryCodeChecker
import com.example.newsapp.adapters.NewsTypeAdapter
import com.example.newsapp.data.UserDataManager
import com.example.newsapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.util.Locale

class NewsActivity : ComponentActivity(), NewsTypeAdapter.OnItemClickListener, ArticlesAdapter.OnArticleClickListener {
    private lateinit var noResultsText: TextView
    private lateinit var newsSearchBar: EditText
    private lateinit var navHeaderText: TextView

    private lateinit var auth: FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navButton: ImageButton

    private val dataSet = listOf(
        "Top Stories", "Business", "General", "Health", "Entertainment",
        "Science", "Sport", "Technology"
    )

    private val apiRequests = APIRequests()
    private val countryCodeChecker = CountryCodeChecker()
    private val userDataManager = UserDataManager()

    private val apiKey = BuildConfig.NEWS_API_KEY
    private var countryCode = "us" // Default case

    // Requests permission from user to use their location for regional news
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLastLocation() // If location is allowed, parse the location and get news based on that
        } else {
            Log.d(TAG, "Location permission denied")

            loadTopStories("us", apiKey) // If region is not allowed, use default (US) location as a basis
        }
    }

    private var articles: List<APIRequests.Article> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsfeed)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager

        val newsTypeAdapter = NewsTypeAdapter(dataSet, this)
        recyclerView.adapter = newsTypeAdapter // Recycler view for the horizontal bar of news type buttons (i.e., Health, Entertainment, etc)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        NavigationMenuListener.setListener(navView, this) // Sets up navigation menu buttons functionality

        val headerView = navView.getHeaderView(0)
        navHeaderText = headerView.findViewById(R.id.navHeaderText)

        navButton = findViewById(R.id.nav_button)
        noResultsText = findViewById(R.id.noResults)
        newsSearchBar = findViewById(R.id.newsSearchBar)

        // Sets up firebase authentication
        auth = Firebase.auth

        // Gets the user's location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check for permissions and request if not granted
        if (checkPermissions()) {
            // Permissions are already granted, so call getLastLocation
            getLastLocation()
        } else {
            // Request permissions using the new API
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Sets up the user greeting on the side bar for this activity
        userDataManager.setupGreeting(this, navHeaderText)

        navButton.setOnClickListener {
            drawerLayout.openDrawer(navView)
        }

        newsSearchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE
                || actionId == EditorInfo.IME_ACTION_GO
                || actionId == EditorInfo.IME_ACTION_SEARCH
            ) { // Checks if the user is done with inputting text into the search bar

                val userInput = newsSearchBar.text.toString() // Gets the user input

                if (userInput != "") { // If the input is not empty, perform an API search based on that input
                    performSearch(apiKey, userInput)
                }

                true
            } else {
                false
            }
        }
    }

    // Loads the top stories for a given country based on the location retrieved
    private fun loadTopStories(countryCode: String, apiKey: String) {
        apiRequests.topStoriesRequest(countryCode, apiKey) { response, errorMessage: String? ->
            runOnUiThread {
                if (errorMessage != null) { // If API limit has been reached
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
                else {
                    articles = response.toList() // Assigns the articles into the global variable
                    val articlesAdapter = ArticlesAdapter(response, this)
                    val newsRecyclerView: RecyclerView = findViewById(R.id.newsArticlesView)

                    newsRecyclerView.layoutManager = LinearLayoutManager(this)
                    newsRecyclerView.adapter = articlesAdapter

                    if (response.isEmpty()) { // If no results, display a message informing the user.
                        // Shouldn't happen in normal use of application for this function
                        noResultsText.text = getString(R.string.no_results_found)
                    }
                    else {
                        noResultsText.text = ""
                    }
                }
            }
        }
    }

    // Loads news articles for a specific search request
    private fun performSearch(apiKey: String, userInput: String) {
        apiRequests.searchRequests(apiKey, userInput) { response, errorMessage: String? ->
            runOnUiThread {
                if (errorMessage != null) { // If API limit has been reached
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
                else {
                    articles = response.toList() // Assigns the articles into the global variable
                    val articlesAdapter = ArticlesAdapter(response, this)
                    val newsRecyclerView: RecyclerView = findViewById(R.id.newsArticlesView)

                    newsRecyclerView.layoutManager = LinearLayoutManager(this)
                    newsRecyclerView.adapter = articlesAdapter

                    if (response.isEmpty()) { // If no results for search, display appropriate message to user
                        noResultsText.text = getString(R.string.no_results_found)
                    }
                    else {
                        noResultsText.text = ""
                    }
                }
            }
        }
    }

    // When one of the horizontal bar options for news type are selected
    override fun onItemClicked(position: Int, data: String) {
        if (data == "Top Stories") { // if top stories button is pressed, load top stories again
            loadTopStories(countryCode, apiKey)
        } else { // If any other button is pressed, category is selected
            apiRequests.categoryRequests(countryCode, data.lowercase(), apiKey) { response, errorMessage: String? -> // data is set to lowercase to match api fields
                runOnUiThread {
                    if (errorMessage != null) { // If API limit has been reached
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                    }
                    else {
                        articles = response.toList() // Assigns the articles into the global variable
                        val articlesAdapter = ArticlesAdapter(response, this)
                        val newsRecyclerView: RecyclerView = findViewById(R.id.newsArticlesView)

                        newsRecyclerView.layoutManager = LinearLayoutManager(this)
                        newsRecyclerView.adapter = articlesAdapter

                        if (response.isEmpty()) { // If there are no results, display appropriate message
                            // Shouldn't happen in normal use of this application
                            noResultsText.text = getString(R.string.no_results_found)
                        }
                        else {
                            noResultsText.text = ""
                        }
                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        if (ActivityCompat.checkSelfPermission( // Re-check location permission
                this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses =
                            geocoder.getFromLocation(location.latitude, location.longitude, 1) // Get address based on lat/lon of location

                        if (!addresses.isNullOrEmpty()) {
                            val country = addresses[0].countryName // Get the country name string from what geocoder provided

                            // Converts from country name to country code for supported regions, if region is unsupported then defaults to US
                            // For example, "United Kingdom" would return "gb" or China would return "cn"
                            countryCode = countryCodeChecker.checkCountryCode(country)

                            loadTopStories(countryCode, apiKey) // Load top stories based on the region retrieved

                        } else {
                            Log.d(TAG, "No address found for the provided coordinates.")

                            loadTopStories(countryCode, apiKey) // Load with default (US) data if no region found
                        }
                    } else {
                        Log.d(TAG, "Location is null")

                        loadTopStories(countryCode, apiKey) // Load with default (US) data if no region found
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Error getting location: ${e.message}", e)

                    loadTopStories(countryCode, apiKey) // Load with default (US) data if no region found
                }
        }
    }

    // Used to check if fine or coarse location permission has been given
    private fun checkPermissions(): Boolean {
        return (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    // Handles the behaviour for if the user clicks on a specific article
    override fun onArticleClick(position: Int) { //
        val clickedArticle = articles[position]

        Log.d(TAG, articles[position].description)

        val articleIntent = Intent(this, ArticleActivity::class.java)
        articleIntent.putExtra("articleData", clickedArticle)
        startActivity(articleIntent)
    }
}