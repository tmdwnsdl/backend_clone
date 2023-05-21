package com.example.chatapplication

// 메시지 데이터 클래스
import java.io.Serializable

data class Message(
    var senderUid: String = "",
    var sended_date: String = "",
    var content: String = "",
    var confirmed:Boolean=false
) : Serializable {
}
