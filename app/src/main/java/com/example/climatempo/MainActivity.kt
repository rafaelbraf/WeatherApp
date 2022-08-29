package com.example.climatempo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import com.example.climatempo.Model.Forecast
import com.example.climatempo.Model.Results
import com.example.climatempo.Model.WeatherMain
import com.example.climatempo.Service.ClimaService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.layout_forecast.view.*
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Error
import java.util.zip.Inflater

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscaCidade("Fortaleza,CE")

    }

    fun buscaCidade(cidade: String) {

        val retrofit = Retrofit.Builder()
            .baseUrl(ClimaService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ClimaService::class.java)

        val call: retrofit2.Call<WeatherMain> = service.getClima(cidade)

        call.enqueue(object: retrofit2.Callback<WeatherMain> {
            override fun onResponse(
                call: retrofit2.Call<WeatherMain>,
                response: retrofit2.Response<WeatherMain>
            ) {
                if (response.isSuccessful) {
                    val resultadoWeatherMain: WeatherMain? = response.body()
                    val results: Results? = resultadoWeatherMain?.results
                    val forecast: ArrayList<Forecast>? = results?.forecast
                    val layoutForecast: LinearLayout = linearLayout_forecasts
                    val forecastHoje = forecast?.get(0)
                    textView_temperatura.text = "${results?.temp}º"
                    textView_cidade.text = results?.cityName
                    textView_descricao.text = results?.description
                    textView_data.text = forecastHoje?.weekday
                    textView_maxima.text = "${forecastHoje?.max.toString()}º"
                    textView_minima.text = "${forecastHoje?.min.toString()}º"
                    textView_umidade.text = "${results?.humidity.toString()}%"
                    textview_velocidadevento.text = results?.windSpeedy
                    println(forecast?.size)
                    val tempoAtual = results?.currently
                    imageView.setImageResource( alteraImagemConformeTempoAtual(tempoAtual.toString(), textView_descricao.text.toString()) )

                    val inflater: LayoutInflater = LayoutInflater.from(this@MainActivity)
                    if (forecast != null) {
                        for (index in forecast) {
                            val view: View = inflater.inflate(R.layout.layout_forecast, layoutForecast, false)
                            view.textView_dia_semana.text = index.weekday
                            val minima = index.min
                            val maxima = index.max
                            var temperaturaMedia: Int = index.calculaTemperaturaMedia(minima, maxima)
                            view.textView_temperatura_media.text = "${temperaturaMedia.toString()}º"
                            layoutForecast.addView(view)
                        }
                    }

                }
            }

            override fun onFailure(call: retrofit2.Call<WeatherMain>, t: Throwable) {
                Log.e("Error", t.stackTraceToString())
            }
        })

    }

    fun alteraImagemConformeTempoAtual(tempoAtual: String, tempoDescricao: String): Int {
        var idDrawable: Int = 0
        if(tempoAtual == "dia" && tempoDescricao.contains("nublado")) {
            idDrawable = R.drawable.dia_nublado
        } else if (tempoAtual == "noite" && !tempoDescricao.contains("nublado") && !tempoDescricao.contains("Neblina")) {
            idDrawable = R.drawable.noite
        } else if (tempoAtual == "noite" && tempoDescricao == "Neblina") {
            idDrawable = R.drawable.noite_nublada
        } else if (tempoAtual == "noite" && tempoDescricao.contains("nublado")) {
            idDrawable = R.drawable.noite_nublada
        }
        return idDrawable
    }

}