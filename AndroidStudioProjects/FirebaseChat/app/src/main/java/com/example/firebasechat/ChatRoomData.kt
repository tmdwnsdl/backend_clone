package com.example.firebasechat

data class ChatRoomData(
    val users: Map<String, Boolean>? = HashMap(),
    var messages: Map<String,ChatData>? = HashMap()
)
