package com.example.chatapplication

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}

    lateinit var btnAddchatRoom: Button
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var recycler_chatroom: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeView()
        //initializeListener()
        setupRecycler()
    }

    fun initializeView() { //뷰 초기화
        try {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference("ChatRoom")!!
            btnAddchatRoom = binding.btnNewMessage
            recycler_chatroom = binding.recyclerChatrooms
        }catch (e:Exception)
        {
            e.printStackTrace()
            Toast.makeText(this,"화면 초기화 중 오류가 발생하였습니다.",Toast.LENGTH_LONG).show()
        }
    }
    /*fun initializeListener()  //버튼 클릭 시 리스너 초기화
    {
        btnAddchatRoom.setOnClickListener()  //새 메시지 화면으로 이동
        {
            startActivity(Intent(this@MainActivity, AddChatRoomActivity::class.java))
            finish()
        }
    }*/

    fun setupRecycler() {
        recycler_chatroom.layoutManager = LinearLayoutManager(this)
        recycler_chatroom.adapter = RecyclerChatRoomsAdapter(this)
    }
}