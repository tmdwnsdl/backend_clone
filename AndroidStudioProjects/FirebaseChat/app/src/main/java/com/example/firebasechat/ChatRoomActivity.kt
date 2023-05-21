package com.example.firebasechat

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.databinding.ActivityChatRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.annotations.Nullable
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class ChatRoomActivity : AppCompatActivity() {
    val binding by lazy { ActivityChatRoomBinding.inflate(layoutInflater) }
    lateinit var btn_exit: ImageButton
    lateinit var btn_send: Button
    lateinit var txt_title: TextView
    lateinit var edt_message: EditText
    lateinit var recycler_talks: RecyclerView
    lateinit var chatRoom: ChatRoomData
    lateinit var opponentUser: User
    lateinit var chatRoomKey: String
    lateinit var myUid: String
    lateinit var firebaseDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initializeProperty()
        initializeView()
        initializeListener()
        setupChatRooms()
        /*// 메시지 전송 버튼 눌렀을 때
        binding.btnSend.setOnClickListener() {
            putMessage()
        }*/
    }
    fun initializeProperty() {  //변수 초기화
        myUid = FirebaseAuth.getInstance().currentUser?.uid!!       //현재 로그인한 유저 id
        firebaseDatabase = FirebaseDatabase.getInstance().reference!!
        chatRoom = (intent.getSerializableExtra("ChatRoom")) as ChatRoomData      //채팅방 정보
        chatRoomKey = intent.getStringExtra("ChatRoomKey")!!            //채팅방 키
        opponentUser = (intent.getSerializableExtra("Opponent")) as User    //상대방 유저 정보
    }
    fun initializeView() {    //뷰 초기화
        btn_exit = binding.imgbtnQuit
        edt_message = binding.editMessage
        recycler_talks = binding.recyclerMessages
        btn_send = binding.btnSend
        txt_title = binding.txtTItle
        //val txt_title.text = opponentUser!!.name ?: ""
    }
    fun initializeListener() {   //버튼 클릭 시 리스너 초기화
        btn_exit.setOnClickListener()
        {
            startActivity(Intent(this@ChatRoomActivity, MainActivity::class.java))
        }
        btn_send.setOnClickListener()
        {
            putMessage()
        }
    }
    fun setupChatRooms() {              //채팅방 목록 초기화 및 표시
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
    }
    // 메세지 전송 함수
    fun putMessage() {
        val msg = binding.editMessage.getText().toString()
        //널이 아닐때만 값 전송하게
        val chat = ChatData(msg, "me", getDateTimeString())
        Log.d("putMessage", chat.msg)
        FirebaseDatabase.getInstance().getReference("messages")
            .push().setValue(chat).addOnSuccessListener {
                Log.i("putMessage", "메시지 전송에 성공하였습니다.")
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
        recycler_talks.adapter = RecyclerMessagesAdapter(this, chatRoomKey, opponentUser.uid)
    }
}

