package com.example.firebasechat

data class ChatData(
    var msg: String = "",
    var username: String = "",
    var send_time: String = "",
    var confirmed:Boolean=false
)

