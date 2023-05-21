package com.example.chatapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.databinding.ItemChatMineBinding
import com.example.chatapp.databinding.ItemChatOthersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlin.collections.ArrayList


class RecyclerMessagesAdapter(
    val context: Context,
    //var chatRoomKey: String?,
    //val opponentUid: String?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var messages: ArrayList<ChatData> = arrayListOf()     //메시지 목록
    var messageKeys: ArrayList<String> = arrayListOf()   //메시지 키 목록
    val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    //val recyclerView = (context as ChatRoomActivity).recycler_talks   //목록이 표시될 리사이클러 뷰

    //메시지의 id에 따라 내 메시지/상대 메시지 구분
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].send_uid.equals(myUid)) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            //메시지가 내 메시지인 경우
            1 -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_chat_mine, parent, false)   //내 메시지 레이아웃으로 초기화

                MyMessageViewHolder(ItemChatMineBinding.bind(view))
            }
            //메시지가 상대 메시지인 경우
            else -> {
                val view =
                    LayoutInflater.from(context)
                        .inflate(R.layout.item_chat_others, parent, false)  //상대 메시지 레이아웃으로 초기화
                OtherMessageViewHolder(ItemChatOthersBinding.bind(view))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (messages[position].send_uid.equals(myUid)) {       //레이아웃 항목 초기화
            (holder as MyMessageViewHolder).bind(position)
        } else {
            (holder as OtherMessageViewHolder).bind(position)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class OtherMessageViewHolder(itemView: ItemChatOthersBinding) :         //상대 메시지 뷰홀더
        RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.bgMessageOthers
        var txtMessage = itemView.txtMessage
        var txtDate = itemView.txtDate
        var txtIsShown = itemView.txtIsShown

        fun bind(position: Int) {
            //메시지 UI 항목 초기화
            var message = messages[position]
            var sendDate = message.send_time

            txtMessage.text = message.msg
            txtDate.text = getDateText(sendDate)

            //확인 여부 표시
            if (message.confirmed.equals(true))
                txtIsShown.visibility = View.GONE
            else
                txtIsShown.visibility = View.VISIBLE

            setShown(position)             //해당 메시지 확인하여 서버로 전송
        }

        //메시지 전송 시각 생성
        fun getDateText(sendDate: String): String {
            var dateText = ""
            var timeString = ""
            if (sendDate.isNotBlank()) {
                timeString = sendDate.substring(8, 12)
                var hour = timeString.substring(0, 2)
                var minute = timeString.substring(2, 4)

                var timeformat = "%02d:%02d"

                if (hour.toInt() > 11) {
                    dateText += "오후 "
                    dateText += timeformat.format(hour.toInt() - 12, minute.toInt())
                } else {
                    dateText += "오전 "
                    dateText += timeformat.format(hour.toInt(), minute.toInt())
                }
            }
            return dateText
        }

        //메시지 확인하여 서버로 전송
        fun setShown(position: Int) {          //메시지 확인하여 서버로 전송
            FirebaseDatabase.getInstance().getReference("ChatRoom")
                .child("chatRooms").child("messages")
                .child(messageKeys[position]).child("confirmed").setValue(true)
                .addOnSuccessListener {
                    Log.i("checkShown", "성공")
                }.addOnCanceledListener {
                    Log.i("checkShown", "실패")
                }
        }
    }

    inner class MyMessageViewHolder(itemView: ItemChatMineBinding) :       // 내 메시지용 ViewHolder
        RecyclerView.ViewHolder(itemView.root) {
        var background = itemView.bgMessageMine
        var txtMessage = itemView.txtMessage
        var txtDate = itemView.txtDate
        var txtIsShown = itemView.txtIsShown

        fun bind(position: Int) {
            //메시지 UI 레이아웃 초기화
            var message = messages[position]
            var sendDate = message.send_time
            txtMessage.text = message.msg

            txtDate.text = getDateText(sendDate)

            if (message.confirmed.equals(true))
                txtIsShown.visibility = View.GONE
            else
                txtIsShown.visibility = View.VISIBLE
        }

        //메시지 전송 시각 생성
        fun getDateText(sendDate: String): String {
            var dateText = ""
            var timeString = ""
            if (sendDate.isNotBlank()) {
                timeString = sendDate.substring(8, 12)
                var hour = timeString.substring(0, 2)
                var minute = timeString.substring(2, 4)

                var timeformat = "%02d:%02d"

                if (hour.toInt() > 11) {
                    dateText += "오후 "
                    dateText += timeformat.format(hour.toInt() - 12, minute.toInt())
                } else {
                    dateText += "오전 "
                    dateText += timeformat.format(hour.toInt(), minute.toInt())
                }
            }
            return dateText
        }
    }

}