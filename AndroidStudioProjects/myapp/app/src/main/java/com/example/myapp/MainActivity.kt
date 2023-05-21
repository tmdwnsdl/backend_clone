package com.example.myapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.commit
import com.example.myapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var myUid: String

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //FirebaseAuth 객체의 인스턴스 가져오기
        auth = FirebaseAuth.getInstance()
        //익명으로 로그인
        Anonymoulsy()


        val fManager = supportFragmentManager
        fManager.commit {
            add(binding.frameLayout.id,MapFragment()) }
        //message버튼을 누르면 채팅 프래그먼트로 이동
        binding.messageButton.setOnClickListener{
            //현재 프래그먼트가 채팅방이 아니라면 이동시키게함
            val currentFragment = fManager.findFragmentById(binding.frameLayout.id)
            if (currentFragment !is ChatFragment) {
                fManager.commit {
                    replace(binding.frameLayout.id,ChatFragment())
                }
            }
        }
        //map 버튼을 누르면 채팅 프래그먼트로 이동
        binding.mapButton.setOnClickListener{
            //현재 프래그먼트가 맵이 아니라면 이동시키게 함
            val currentFragment = fManager.findFragmentById(binding.frameLayout.id)
            if (currentFragment !is MapFragment){
                fManager.commit {
                    replace(binding.frameLayout.id,MapFragment())
                }
            }
        }
    }
    // Firebase Authentication 익명으로 로그인
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
            myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
            Log.d("Login", myUid)
        }
}
}