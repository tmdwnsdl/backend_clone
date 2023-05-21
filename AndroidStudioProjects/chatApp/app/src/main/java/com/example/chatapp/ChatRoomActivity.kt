package com.example.chatapp

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ActivityChatRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ChatRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityChatRoomBinding.inflate(layoutInflater) }
    lateinit var myUid: String
    lateinit var recycler_talks: RecyclerView
    lateinit var chatRoom: ChatRoomData
    //lateinit var chatRoomKey: String
    lateinit var opponentUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //setupChatRooms()

        // 메시지 전송 버튼 눌렀을 때
        binding.btnSend.setOnClickListener() {
            putMessage()
        }

    }
    /*fun setupChatRooms() {              //채팅방 목록 초기화 및 표시
        if (chatRoomKey.isNullOrBlank())
            setupChatRoomKey()
        else
            setupRecycler()
    }
    fun setupChatRoomKey() {            //chatRoomKey 없을 경우 초기화 후 목록 초기화
        FirebaseDatabase.getInstance().getReference("ChatRoom")
            .child("chatRooms").orderByChild("users/${opponentUser.uid}")
            .equalTo(true)    //상대방의 Uid가 포함된 목록이 있는지 확인
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        chatRoomKey = data.key!!          //chatRoomKey 초기화
                        setupRecycler()                  //목록 업데이트
                        break
                    }
                }
            })
    }*/
    // 메세지 전송 함수
    fun putMessage() {
        val msg = binding.editMessage.getText().toString()
        myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        //널이 아닐때만 값 전송하게
        val chat = ChatData(msg, myUid, getDateTimeString())
        Log.d("putMessage", chat.msg)
        FirebaseDatabase.getInstance().getReference("messages")
            .push().setValue(chat).addOnSuccessListener {
                Log.i("putMessage", "메시지 전송에 성공하였습니다.")
                Log.i("putMessage",getDateTimeString())
                binding.editMessage.text.clear()
            }.addOnCanceledListener {
                Log.i("putMessage", "메시지 전송에 실패하였습니다")
            }

    }
    fun getDateTimeString(): String {          //메시지 보낸 시각 정보 반환
        var localDateTime = LocalDateTime.now()
        localDateTime.atZone(TimeZone.getDefault().toZoneId())
        var dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        return localDateTime.format(dateTimeFormatter).toString()
    }
    fun setupRecycler() {            //목록 초기화 및 업데이트
        recycler_talks.layoutManager = LinearLayoutManager(this)
        recycler_talks.adapter = RecyclerMessagesAdapter(this)
    }
}