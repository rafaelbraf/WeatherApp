package com.example.climatempo.Model

import com.google.gson.annotations.SerializedName

data class Forecast(

    @SerializedName("date"        ) var date        : String? = null,
    @SerializedName("weekday"     ) var weekday     : String? = null,
    @SerializedName("max"         ) var max         : Int?    = null,
    @SerializedName("min"         ) var min         : Int?    = null,
    @SerializedName("description" ) var description : String? = null,
    @SerializedName("condition"   ) var condition   : String? = null

)
