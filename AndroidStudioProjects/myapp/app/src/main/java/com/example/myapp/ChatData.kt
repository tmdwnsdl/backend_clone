package com.example.myapp

// 저장되어야 할 메시지 하나하나의 data
data class ChatData(
    val send_uid: String = "",
    val msg: String = "",
    val send_time: String = "",
    val confirmed:Boolean=false
)