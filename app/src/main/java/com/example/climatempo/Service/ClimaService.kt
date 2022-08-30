package com.example.climatempo.Service

import com.example.climatempo.Model.Results
import com.example.climatempo.Model.WeatherMain
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaService {

    companion object {
        const val BASE_URL = "https://api.hgbrasil.com/"
    }

    @GET("weather?key={YOUR_KEY}")
    fun getClima(@Query("city_name") cidade: String): Call<WeatherMain>

}