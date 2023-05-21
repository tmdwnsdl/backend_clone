package com.example.firebasechat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasechat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var firebaseDatabase: DatabaseReference
    lateinit var recycler_chatroom: RecyclerView
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        initializeView()
        initializeListener()
        setupRecycler()
        binding.btnanonymo.setOnClickListener{
            Anonymoulsy()
        }
        /*binding.btnChat.setOnClickListener {
            val intent = Intent(this, ChatRoomActivity::class.java)
            startActivity(intent)
        }*/
    }

    fun initializeView() { //뷰 초기화
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("ChatRoom")!!
        recycler_chatroom = binding.recyclerChatrooms
    }
    fun initializeListener()  //버튼 클릭 시 리스너 초기화
    {
        binding.btnChat.setOnClickListener()  //새 메시지 화면으로 이동
        {
            startActivity(Intent(this@MainActivity, ChatRoomActivity::class.java))
            finish()
        }
    }
    fun setupRecycler() {
        recycler_chatroom.layoutManager = LinearLayoutManager(this)
        recycler_chatroom.adapter = RecyclerChatRoomAdapter(this)
    }
    fun Anonymoulsy() {
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Login", "signInAnonymously:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Login", "signInAnonymously:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    updateUI(null)
                }
            }

    }
    private fun updateUI(user: FirebaseUser?) { //update ui code here
        if (user != null) {
            val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        }
    }
}



