package com.example.assignment2

import android.content.Intent
import android.content.Intent.ACTION_DIAL
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
        //activity Result API
        val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK) {          //resultCode가 RESULT_OK와 같을 때
                Log.d("IISE", "${it.resultCode}")       //result code 출력
                Log.d("IISE","${it.data?.getStringExtra("phone number")}")  //받은 phonenumber의 value값을 추출
                Toast.makeText(this, it.data?.getStringExtra("phone number"), Toast.LENGTH_SHORT).show()
                //toast를 활용하여 phone number의 value값을 화면 밑에 띄움
            }else{      //resultCode가 RESULT_OK와 다를 때
                Log.d("IISE", "${it.resultCode}")       //result code 출력
        } }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            val binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            binding.btnReply.setOnClickListener {
                val phoneNumber = binding.txtReply.text.toString()      //입력한 전화번호 변수 phuoneNumber에 저장
                val intent = Intent(Intent.ACTION_DIAL).apply {      //implicit intent 통화 기능을 수행할 수 있는 앱
                        data = Uri.parse("tel:$phoneNumber") }      //Data URI Scheme tel에 phoneNumber 저장
                val chooser = Intent.createChooser(intent, "Which app to use?")      //app chooser dialog 이용
                requestLauncher.launch(chooser)     //선택한 앱 실행
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