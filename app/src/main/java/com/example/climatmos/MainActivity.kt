package com.example.climatmos
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.example.climatmos.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.locks.Condition

//API KEY 53598c43bdf72f6ded35c3b6fa4a2cf8

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fetchWeatherData("Varanasi")
        SearchCity()
    }

    private fun SearchCity() {
        val searchView=binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextChange(query: String?): Boolean {
                if (query!=null){
                    fetchWeatherData(query)
                }
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })
    }

    private fun fetchWeatherData(cityName: String){
        val retrofit= Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .build().create(ApiInterface::class.java)

        val response=retrofit.getWeatherData(cityName,"53598c43bdf72f6ded35c3b6fa4a2cf8","metric")
        response.enqueue(object : Callback<weather_climatmos>{
            override fun onResponse(call: Call<weather_climatmos?>, response: Response<weather_climatmos?>) {
                val responseBody=response.body()
                if (response.isSuccessful && responseBody!=null){
                    val temperature=responseBody.main.temp.toString()
                    val humidity=responseBody.main.humidity
                    val windSpeed=responseBody.wind.speed
                    val sunrise=responseBody.sys.sunrise.toLong()
                    val sunset=responseBody.sys.sunset.toLong()
                    val seaLevel=responseBody.main.pressure
                    val condition= responseBody.weather.firstOrNull()?.main?:"Unknown"
                    val maxTemp=responseBody.main.temp_max
                    val minTemp=responseBody.main.temp_min

                    binding.temp.text="$temperature °C"
                    binding.weather.text=condition
                    binding.maxTemp.text="Max Temp: $maxTemp °C"
                    binding.minTemp.text="Min Temp: $minTemp °C"
                    binding.humidity.text="$humidity %"
                    binding.windSpeed.text="$windSpeed M/s"
                    binding.sunrise.text="${time(sunrise)}"
                    binding.sunset.text="${time(sunset)}"
                    binding.sea.text="$seaLevel hPa"
                    binding.condition.text=condition
                    binding.cityName.text="$cityName"
                    binding.day.text=dayName(System.currentTimeMillis())
                    binding.date.text=date()
//                    Log.d("TAG", "onResponse: $temperature")
                    changeImagesaccordingtoweatherconditions(condition)
                }
            }

            override fun onFailure(
                call: Call<weather_climatmos?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
    private fun changeImagesaccordingtoweatherconditions(conditions: String) {
        when(conditions){
            "Clear Sky","Sunny","Clear"->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
            "Partly Clouds","Clouds","Overcast","Mist","Foggy","Haze"->{
                binding.root.setBackgroundResource(R.drawable.colud_background)
                binding.lottieAnimationView.setAnimation(R.raw.cloud)
            }
            "Light Rain","Drizzle","Moderate Rain","Showers","Heavy Rain"->{
                binding.root.setBackgroundResource(R.drawable.rain_background)
                binding.lottieAnimationView.setAnimation(R.raw.rain)
            }
            "Light Snow","Moderate Snow","Blizzard","Heavy Snow"->{
                binding.root.setBackgroundResource(R.drawable.snow_background)
                binding.lottieAnimationView.setAnimation(R.raw.snow)
            }
            else ->{
                binding.root.setBackgroundResource(R.drawable.sunny_background)
                binding.lottieAnimationView.setAnimation(R.raw.sun)
            }
        }
        binding.lottieAnimationView.playAnimation()
    }

    private fun date():String{
        val sdf= SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return sdf.format((Date()))
    }
    private fun time(timestamp:Long):String{
        val sdf= SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format((Date(timestamp*1000)))
    }

    fun dayName(timestamp:Long): String{
        val sdf= SimpleDateFormat("EEEE", Locale.getDefault())
        return sdf.format((Date()))
    }
}