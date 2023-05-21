package com.example.assignment2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.assignment2.databinding.ActivitySubBinding


class SubActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("IISE", "SubActivity created")

        val phoneNum = intent.data.toString()   //전화번호를 입력받음
        Log.d("IISE", phoneNum)      //전화번호 출력

        intent.putExtra("phone number", "You can't call to $phoneNum!!!!!!!")
        //putExtra를 활용하여 toast 되어야 할 value 저장
        setResult(RESULT_OK, intent)        //결과를 내보냄
        finish()    //종료
        }
    }