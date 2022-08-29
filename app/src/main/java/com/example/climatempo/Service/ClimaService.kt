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

    //https://api.hgbrasil.com/weather?key=5caee7bd&city_name=$cidade
    @GET("/weather?key=5caee7bd&city_name=")
    fun getWheaterByCity(@Query("q") q: String, callback: Call<WeatherMain>)

    @GET("weather?key=5caee7bd&")
    fun getClima(@Query("city_name") cidade: String): Call<WeatherMain>

    @GET("weather?key=5caee7bd&")
    fun getResults(@Query("city_name") cidade: String): Call<Results>

}