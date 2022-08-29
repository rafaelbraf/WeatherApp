package com.example.climatempo.Model

import com.google.gson.annotations.SerializedName

data class WeatherMain(

    @SerializedName("by"             ) var by            : String?  = null,
    @SerializedName("valid_key"      ) var validKey      : Boolean? = null,
    @SerializedName("results"        ) var results       : Results? = Results(),
    @SerializedName("execution_time" ) var executionTime : Int?     = null,
    @SerializedName("from_cache"     ) var fromCache     : Boolean? = null

)
