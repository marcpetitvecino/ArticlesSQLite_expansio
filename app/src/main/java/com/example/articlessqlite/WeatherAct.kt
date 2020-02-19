package com.example.articlessqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.articlessqlite.Interfaces.HTTPService
import com.example.articlessqlite.WeatherClasses.JSON
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.round

class WeatherAct : AppCompatActivity() {

    private lateinit var cityET: EditText
    private lateinit var searchBtn: Button
    var city = ""
    private lateinit var cityTitle: TextView
    private lateinit var weatherImage: ImageView
    private lateinit var weatherImageDescription: TextView
    private lateinit var cityMinTemp: TextView
    private lateinit var cityMaxTemp: TextView
    private lateinit var cityHumidity: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)
        initViews()
        initListeners()

    }

    private fun initViews() {

        cityET = findViewById(R.id.inputCityET)
        searchBtn = findViewById(R.id.buttonBuscar)
        cityTitle = findViewById(R.id.cityTitle)
        weatherImage = findViewById(R.id.weatherImage)
        weatherImageDescription = findViewById(R.id.weatherImageDescription)
        cityMinTemp = findViewById(R.id.cityMinTemp)
        cityMaxTemp = findViewById(R.id.cityMaxTemp)
        cityHumidity = findViewById(R.id.cityHumidity)

    }

    private fun initListeners() {

        searchBtn.setOnClickListener {

            city = cityET.text.toString()
            loadAPI()

        }

    }

    private fun loadAPI() {

        val retrofit = Retrofit.Builder()
            .baseUrl("http://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val httpService = retrofit.create(HTTPService::class.java)
        val call: Call<JSON> = httpService.getJSON("weather?q=$city&appid=02fb550982a985bc4ce24704ca47921a")

        call.enqueue(object: Callback<JSON> {
            override fun onFailure(call: Call<JSON>?, t: Throwable) {

                cityTitle.text = t.message

            }

            override fun onResponse(call: Call<JSON>?, response: Response<JSON>?) {

                if (!response!!.isSuccessful) {

                    cityTitle.text = "Error ${response.code()}"

                    return

                }

                val JSONList = response.body()

                if (JSONList != null) {
                    val currentWeather = JSONList

                    val max:Double = currentWeather.main.temp_max-273.15
                    val number3digits:Double = Math.round(max * 1000.0) / 1000.0
                    val number2digits:Double = Math.round(number3digits * 100.0) / 100.0
                    val solutionMax:Double = Math.round(number2digits * 10.0) / 10.0

                    val min:Double = currentWeather.main.temp_min-273.15
                    val number3digitsm:Double = Math.round(min * 1000.0) / 1000.0
                    val number2digitsm:Double = Math.round(number3digitsm * 100.0) / 100.0
                    val solutionMin:Double = Math.round(number2digitsm * 10.0) / 10.0

                    cityTitle.text = "Temps actual a ${currentWeather.name}"
                    val uri = "https://openweathermap.org/img/wn/" + currentWeather.weather[0].icon + "@2x.png"
                    Picasso.get().load(uri).into(weatherImage)
                    weatherImageDescription.text = currentWeather.weather[0].main

                    cityMinTemp.text = "Temperatura mínima: ${solutionMin}º"
                    cityMaxTemp.text = "Temperatura màxima: ${solutionMax}º"
                    cityHumidity.text = "Humitat: ${currentWeather.main.humidity}%"


                }

            }


        })

    }

}
