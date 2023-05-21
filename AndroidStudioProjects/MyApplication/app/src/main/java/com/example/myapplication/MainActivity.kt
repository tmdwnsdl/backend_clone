package com.example.myapplication

import android.app.SearchManager
import android.content.Intent
import android.content.Intent.createChooser
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.myapplication.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    //activity Result API start
    val requestLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        Log.d("IISE","${it.resultCode}")
        Log.d("IISE", "${it.data?.getStringExtra("grade")}")
        Toast.makeText(this, it.data?.getStringExtra("grade"), Toast.LENGTH_SHORT).show()
    }
    val uriLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        Log.d("IISE", it.toString())
    }
    //basic code
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("IISE","onCreate() Called!")

        val intent = Intent(this, MainActivity2::class.java)
        binding.btnSay.setOnClickListener { requestLauncher.launch(intent) }
        //Activity Result API finish

        //getting a result from an activity
        /*val intent = Intent(this, MainActivity2::class.java)
        binding.btnSay.setOnClickListener {startActivityForResult(intent,1000)}*/

        /*binding.btnSay.setOnClickListener{
            binding.textSay.text="I love Android!"}*/

        /*val intent = Intent(this, MainActivity2::class.java)
        binding.btnSay.setOnClickListener {startActivity(intent)}*/

        /*val intent = Intent(Intent.ACTION_DIAL).apply{
            data = Uri.parse("tel:01028410460") }
        binding.btnSay.setOnClickListener {startActivity(intent)}*/

        /*val intent = Intent(Intent.ACTION_VIEW).apply{
            data = Uri.parse("geo:37.63177,127.077") }
        binding.btnSay.setOnClickListener {startActivity(intent)}*/

        /*val intent = Intent(Intent.ACTION_WEB_SEARCH).apply{
            putExtra(SearchManager.QUERY,"seoultech IISE") }
        binding.btnSay.setOnClickListener {startActivity(intent)}*/

        /*val intent = Intent(Intent.ACTION_SEND).apply{
            type = "image/png" }
        binding.btnSay.setOnClickListener {startActivity(intent)}*/

        /*val intent = Intent(Intent.ACTION_VIEW)
        val chooser = Intent.createChooser(intent, "show me the Picture!")
        binding.btnSay.setOnClickListener {startActivity(chooser)}*/

        /*val intent = Intent(Intent.ACTION_VIEW)
            .run{createChooser(this,"Show me the Picture")}
        binding.btnSay.setOnClickListener {startActivity(intent)}*/

        //basic Log
        //Log.d("IISE","onCreate() Called!")



    }
    //getting a result from an activity
    /*override fun onActivityResult(requestCode: Int, resultCode:Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("IISE", "requestCode: $requestCode resultCode: $resultCode")
        Log.d("IISE", "${data?.getStringExtra("grade")}")
        Toast.makeText(this, data?.getStringExtra("grade"), Toast.LENGTH_SHORT).show()
    }*/
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
