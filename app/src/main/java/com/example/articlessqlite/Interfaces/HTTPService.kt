package com.example.articlessqlite.Interfaces

import com.example.articlessqlite.WeatherClasses.JSON
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url


interface HTTPService {

    @GET
    fun getJSON(@Url url: String): Call<JSON>


}