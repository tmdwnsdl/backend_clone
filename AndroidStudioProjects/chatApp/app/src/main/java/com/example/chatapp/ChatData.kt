package com.example.chatapp

data class ChatData(
    var msg: String = "",
    var send_uid: String = "",
    var send_time: String = "",
    var confirmed:Boolean=false
)
