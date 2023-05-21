package com.example.myapp

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapp.databinding.ItemChatMineBinding
import com.example.myapp.databinding.ItemChatOthersBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val messages: ArrayList<ChatData> = arrayListOf()     //메시지 목록
    //var messageKeys: ArrayList<String> = arrayListOf()   //메시지 키 목록
    val myUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference()

    fun addItem(item: ChatData){
        messages.add(item)
    }

    //메시지의 id에 따라 내 메시지/상대 메시지 구분
    override fun getItemViewType(position: Int): Int {
        return if (messages[position].send_uid.equals(myUid)) 1 else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            //메시지가 내 메시지인 경우
            1 -> {
                val binding = ItemChatMineBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return MyMessageViewHolder(binding)
            }
            //메시지가 상대 메시지인 경우
            else -> {
                val binding = ItemChatOthersBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                return OthersMessageViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (messages[position].send_uid.equals(myUid)) {
            (holder as MyMessageViewHolder).bind(messages[position])
        } else {
            (holder as OthersMessageViewHolder).bind(messages[position])
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    inner class MyMessageViewHolder(val binding: ItemChatMineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatData) {
            //메시지 UI 항목 초기화
            binding.txtMessage.text = chat.msg
            binding.txtDate.text = getDateText(chat.send_time)
            //확인 여부 표시
            if (chat.confirmed.equals(true))
                binding.txtIsShown.visibility = View.GONE
            else
                binding.txtIsShown.visibility = View.VISIBLE
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

    inner class OthersMessageViewHolder(private val binding: ItemChatOthersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: ChatData) {
            //메시지 UI 항목 초기화
            binding.txtMessage.text = chat.msg
            binding.txtDate.text = getDateText(chat.send_time)
            //확인 여부 표시
            if (chat.confirmed.equals(true))
                binding.txtIsShown.visibility = View.GONE
            else
                binding.txtIsShown.visibility = View.VISIBLE
        }

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