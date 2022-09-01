package com.example.climatempo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
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

                    if (results != null && forecastHoje != null) preencheResultadoNaTela(results, forecastHoje)

                    val tempoAtual = results?.currently
                    val codCondicao = results?.conditionCode
                    if (tempoAtual != null && codCondicao != null) imageView.setImageResource( alteraImagemConformeTempoAtual(codCondicao, tempoAtual) )

                    val inflater: LayoutInflater = LayoutInflater.from(this@MainActivity)
                    if (forecast != null) {
                        for (index in forecast.take(4)) {
                            val view: View = inflater.inflate(R.layout.layout_forecast, layoutForecast, false)
                            view.textView_dia_semana.text = index.weekday
                            var temperaturaMedia: Int = index.calculaTemperaturaMedia(index.min, index.max)
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

    fun alteraImagemConformeTempoAtual(codCondicao: String, tempoAtual: String): Int {

        return if (tempoAtual == "dia") {
            when (codCondicao) {
                "32", "33", "34", "27", "31", "35", "40", "45" -> R.drawable.sol
                "25", "26", "28", "29", "30" -> R.drawable.dia_nublado
                "9", "11", "12" -> R.drawable.dia_nublado_com_chuva
                else -> R.drawable.sol
            }
        } else {
            when (codCondicao) {
                "26", "28", "29", "30" -> R.drawable.noite_nublada
                "33", "27", "31" -> R.drawable.noite
                else -> R.drawable.noite
            }
        }

    }

    fun preencheResultadoNaTela(result: Results, forecastHoje: Forecast) {
        textView_temperatura.text = "${result.temp}º"
        textView_cidade.text = result.cityName
        textView_descricao.text = result.description
        textView_data.text = forecastHoje.weekday
        textView_maxima.text = "${forecastHoje.max.toString()}º"
        textView_minima.text = "${forecastHoje.min.toString()}º"
        textView_umidade.text = "${result.humidity.toString()}%"
        textview_velocidadevento.text = result.windSpeedy
    }

    fun buscarCidade(view: View) {
        Toast.makeText(this, "Clicou no botão de pesquisar", Toast.LENGTH_SHORT).show()
    }

}