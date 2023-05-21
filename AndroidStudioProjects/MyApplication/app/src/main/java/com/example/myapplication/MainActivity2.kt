package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.databinding.ActivityMain2Binding
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("IISE","SubActivity created!")

        binding.btnReply.setOnClickListener {
            intent.putExtra("grade", "${binding.txtReply.text}")
            setResult(RESULT_OK, intent)
            finish()
        }

    }
}