package com.example.climatempo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        val activAcionada = intent.getStringExtra("activity")
        val cidadePesquisada = intent.getStringExtra("cidade_pesquisada")

        println("$activAcionada - $cidadePesquisada")

        if (activAcionada == "activity_search" && cidadePesquisada != null && cidadePesquisada != "") buscaCidade(cidadePesquisada)
        else buscaCidade("Fortaleza,CE")

        imageButton_search.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }

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

                    if (results != null && forecast != null) preencheResultadoNaTela(results, forecast[0])

                    val tempoAtual = results?.currently
                    imageView.setImageResource( alteraImagemConformeTempoAtual(tempoAtual.toString(), textView_descricao.text.toString()) )

                    val inflater: LayoutInflater = LayoutInflater.from(this@MainActivity)
                    if (forecast != null) {
                        for (index in forecast) {
                            val view: View = inflater.inflate(R.layout.layout_forecast, layoutForecast, false)
                            view.textView_dia_semana.text = index.weekday
                            view.textView_temperatura_media.text = "${index.calculaTemperaturaMedia(index.min, index.max).toString()}º"
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

    fun preencheResultadoNaTela(results: Results, forecast: Forecast) {
        textView_temperatura.text = "${results.temp}º"
        textView_cidade.text = results.cityName
        textView_descricao.text = results.description
        textView_data.text = forecast.weekday
        textView_maxima.text = "${forecast.max.toString()}º"
        textView_minima.text = "${forecast.min.toString()}º"
        textView_umidade.text = "${results.humidity.toString()}%"
        textview_velocidadevento.text = results?.windSpeedy
    }

}