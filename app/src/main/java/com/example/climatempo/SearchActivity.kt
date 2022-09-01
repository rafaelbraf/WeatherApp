package com.example.climatempo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.example.climatempo.Model.Forecast
import com.example.climatempo.Model.Results
import com.example.climatempo.Model.WeatherMain
import com.example.climatempo.Service.ClimaService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.layout_forecast.view.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        imageButton_back.setOnClickListener {
            finish()
        }

        button_pesquisar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("activity", "activity_search")
            intent.putExtra("cidadePesquisada", editText_busca_cidade.text.toString())
            startActivity(intent)
            finish()
        }

    }

}