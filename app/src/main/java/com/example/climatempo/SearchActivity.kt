package com.example.climatempo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_search.*

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
            intent.putExtra("cidade_pesquisada", editText_cidade_pesquisada.text.toString())
            startActivity(intent)
            finish()
        }

    }
}