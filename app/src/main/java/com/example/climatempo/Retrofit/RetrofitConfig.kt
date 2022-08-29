package com.example.climatempo.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.hgbrasil.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}