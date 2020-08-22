package com.kesatriakeyboard.chatapp.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kesatriakeyboard.chatapp.databinding.AdapterMyChatBinding
import com.kesatriakeyboard.chatapp.databinding.AdapterOtherChatBinding
import com.kesatriakeyboard.chatapp.model.Chat

class MainAdapter(
    private val userName: String,
    private val viewModel: MainViewModel
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MY_CHAT = 0
    private val OTHER_CHAT = 1

    private var chatList: List<Chat> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == MY_CHAT) {
            val view = AdapterMyChatBinding.inflate(inflater, parent, false)
            MyChatHolder(view.root, view)
        } else {
            val view = AdapterOtherChatBinding.inflate(inflater, parent, false)
            OtherChatHolder(view.root, view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = chatList[position]
        if (holder is MyChatHolder) {
            holder.bindView(chat)
        } else if (holder is OtherChatHolder) {
            holder.bindView(chat)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun getItemViewType(position: Int): Int {
        val chat = chatList[position]
        return if (chat.sender.orEmpty() == userName) {
            MY_CHAT
        } else OTHER_CHAT
    }

    fun setChatDataList(chatList: List<Chat>) {
        this.chatList = chatList
        notifyDataSetChanged()
    }

    inner class MyChatHolder(
        view: View,
        val binding: AdapterMyChatBinding
    ): RecyclerView.ViewHolder(view) {
        fun bindView(chat: Chat) {
            binding.sender.text = chat.sender.orEmpty()
            binding.message.text = chat.message.orEmpty()

            binding.bubble.setOnLongClickListener {
                viewModel.onChatLongPress(chat)
                true
            }
        }
    }

    class OtherChatHolder(
        view: View,
        val binding: AdapterOtherChatBinding
    ): RecyclerView.ViewHolder(view) {
        fun bindView(chat: Chat) {
            binding.sender.text = chat.sender.orEmpty()
            binding.message.text = chat.sender.orEmpty()
        }
    }
}