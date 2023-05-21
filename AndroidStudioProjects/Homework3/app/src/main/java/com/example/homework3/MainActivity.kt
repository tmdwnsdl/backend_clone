package com.example.homework3

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.homework3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReply.setOnClickListener {
            // 임의의 색상 생성
            val randomColor = Color.rgb((0..255).random(), (0..255).random(), (0..255).random())

            // 텍스트 및 색상 변경
            binding.textSay.setTextColor(randomColor)

            // RGB 값을 텍스트로 표시
            val redValue = Color.red(randomColor)
            val greenValue = Color.green(randomColor)
            val blueValue = Color.blue(randomColor)
            binding.textSay.setText("COLOR: $redValue r, $greenValue g, $blueValue b")
        }
    }
    override fun onStart(){
        super.onStart()
        Log.d("IISE","onStart() Called!")
    }
    override fun onResume(){
        super.onResume()
        Log.d("IISE","onResume() Called!")
    }
    override fun onPause(){
        super.onPause()
        Log.d("IISE","onPause() Called!")
    }
    override fun onStop(){
        super.onStop()
        Log.d("IISE","onStop() Called!")
    }
    override fun onDestroy(){
        super.onDestroy()
        Log.d("IISE","onDestroy() Called!")
    }
    override fun onRestart(){
        super.onRestart()
        Log.d("IISE","onRestart() Called!")
    }
}