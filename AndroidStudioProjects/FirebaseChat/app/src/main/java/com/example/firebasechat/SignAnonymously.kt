package com.example.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebasechat.databinding.ActivitySignAnonymouslyBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignAnonymously : AppCompatActivity() {
    lateinit var binding: ActivitySignAnonymouslyBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignAnonymouslyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        binding.btnAnonym.setOnClickListener()
        {
            Anonymoulsy()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        }
    }
}