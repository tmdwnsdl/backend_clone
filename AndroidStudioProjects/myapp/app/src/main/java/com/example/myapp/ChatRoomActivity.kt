package com.example.myapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapp.databinding.ActivityChatRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


//채팅방에 대한 activity
class ChatRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityChatRoomBinding.inflate(layoutInflater) }
    lateinit var myUid: String
    lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // 메시지 전송 버튼 눌렀을 때
        binding.btnSend.setOnClickListener() {
            putMessage()
        }
    }
    //메세지 전송 함수
    fun putMessage() {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference()
        val msg = binding.editMessage.getText().toString()
        myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val chat = ChatData(myUid, msg, getDateTimeString())
        Log.d("putMessage", chat.msg)
        Log.d("putMessage", getDateTimeString())

        myRef.child("message").push().setValue(chat)
        binding.editMessage.text.clear()    //입력한 메시지 삭제

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val adapter = MessageAdapter()
                adapter.messages.clear()
                adapter.apply{
                    adapter.addItem(chat)
                }
                adapter.notifyDataSetChanged()          //화면 업데이트
                binding.recyclerMessage.scrollToPosition(adapter.messages.size - 1)    //스크롤 최 하단으로 내리기
                binding.recyclerMessage.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    //메시지 보낸 시각 정보 반환
    fun getDateTimeString(): String {
        var localDateTime = LocalDateTime.now()
        localDateTime.atZone(TimeZone.getDefault().toZoneId())
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        return localDateTime.format(dateTimeFormatter).toString()
    }

    }

