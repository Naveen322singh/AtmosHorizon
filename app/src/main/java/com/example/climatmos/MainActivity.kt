package com.example.climatmos

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.climatmos.databinding.ActivityMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//API KEY 53598c43bdf72f6ded35c3b6fa4a2cf8

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    // Location services
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_CODE = 1001
    private var lastSearchedCity = "Varanasi" // Default city

    // For saving instance state
    companion object {
        private const val LAST_CITY_KEY = "last_city"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Restore last searched city if available
        savedInstanceState?.let {
            lastSearchedCity = it.getString(LAST_CITY_KEY, "Varanasi")
        }

        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fetchWeatherData(lastSearchedCity)
        setupSearchView()
        setupPullToRefresh()

        // Add location button functionality
        binding.locationButton.setOnClickListener {
            getCurrentLocationWeather()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_CITY_KEY, lastSearchedCity)
    }

    private fun setupSearchView() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(query: String?): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    lastSearchedCity = query
                    fetchWeatherData(query)
                    // Hide keyboard after search
                    hideKeyboard()
                    searchView.clearFocus()
                }
                return true
            }
        })
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
    }

    private fun setupPullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            fetchWeatherData(lastSearchedCity)
        }
    }

    private fun fetchWeatherData(cityName: String) {
        // Show loading indicator
        binding.progressBar.visibility = android.view.View.VISIBLE

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherData(cityName, "53598c43bdf72f6ded35c3b6fa4a2cf8", "metric")
        response.enqueue(object : Callback<weather_climatmos> {
            override fun onResponse(call: Call<weather_climatmos?>, response: Response<weather_climatmos?>) {
                // Hide loading indicators
                binding.progressBar.visibility = android.view.View.GONE
                binding.swipeRefreshLayout.isRefreshing = false

                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    updateUI(responseBody, cityName)
                } else {
                    showError("Failed to get weather data. Please try again.")
                    Log.e("WeatherApp", "API response error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<weather_climatmos?>, t: Throwable) {
                // Hide loading indicators
                binding.progressBar.visibility = android.view.View.GONE
                binding.swipeRefreshLayout.isRefreshing = false

                showError("Network error. Please check your connection.")
                Log.e("WeatherApp", "API call failed: ${t.message}")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(responseBody: weather_climatmos, cityName: String) {
        val temperature = responseBody.main.temp.toString()
        val humidity = responseBody.main.humidity
        val windSpeed = responseBody.wind.speed
        val sunrise = responseBody.sys.sunrise.toLong()
        val sunset = responseBody.sys.sunset.toLong()
        val seaLevel = responseBody.main.pressure
        val condition = responseBody.weather.firstOrNull()?.main ?: "Unknown"
        val maxTemp = responseBody.main.temp_max
        val minTemp = responseBody.main.temp_min

        binding.temp.text = "$temperature °C"
        binding.weather.text = condition
        binding.maxTemp.text = "Max Temp: $maxTemp °C"
        binding.minTemp.text = "Min Temp: $minTemp °C"
        binding.humidity.text = "$humidity %"
        binding.windSpeed.text = "$windSpeed M/s"
        binding.sunrise.text = time(sunrise)
        binding.sunset.text = time(sunset)
        binding.sea.text = "$seaLevel hPa"
        binding.condition.text = condition
        binding.cityName.text = cityName
        binding.day.text = dayName(System.currentTimeMillis())
        binding.date.text = date()

        changeImagesAccordingToWeatherConditions(condition)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun changeImagesAccordingToWeatherConditions(conditions: String) {
        when (conditions) {
            "Clear Sky", "Sunny", "Clear" -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Partly Clouds", "Clouds", "Overcast", "Mist", "Foggy", "Haze" -> {
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain", "Drizzle", "Moderate Rain", "Showers", "Heavy Rain" -> {
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow", "Moderate Snow", "Blizzard", "Heavy Snow" -> {
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else -> {
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date(): String {
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }

    private fun time(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp * 1000)))
    }

    private fun dayName(timestamp: Long): String {
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }

    // Location-based weather methods
    private fun getCurrentLocationWeather() {
        if (checkLocationPermission()) {
            if (isLocationEnabled()) {
                requestLocation()
            } else {
                showError("Please enable location services")
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_CODE
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocation() {
        binding.progressBar.visibility = android.view.View.VISIBLE

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    fusedLocationClient.removeLocationUpdates(this)

                    val location = locationResult.lastLocation
                    if (location != null) {
                        getWeatherByCoordinates(location.latitude, location.longitude)
                    } else {
                        binding.progressBar.visibility = android.view.View.GONE
                        showError("Unable to get location")
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun getWeatherByCoordinates(latitude: Double, longitude: Double) {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response = retrofit.getWeatherByCoordinates(
            latitude,
            longitude,
            "53598c43bdf72f6ded35c3b6fa4a2cf8",
            "metric"
        )

        response.enqueue(object : Callback<weather_climatmos> {
            override fun onResponse(call: Call<weather_climatmos?>, response: Response<weather_climatmos?>) {
                binding.progressBar.visibility = android.view.View.GONE

                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    // Extract city name from response if available
                    val cityName = responseBody.name ?: "Current Location"
                    lastSearchedCity = cityName
                    updateUI(responseBody, cityName)
                } else {
                    showError("Failed to get location weather data")
                }
            }

            override fun onFailure(call: Call<weather_climatmos?>, t: Throwable) {
                binding.progressBar.visibility = android.view.View.GONE
                showError("Network error. Please check your connection.")
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationWeather()
            } else {
                showError("Location permission denied")
            }
        }
    }
}