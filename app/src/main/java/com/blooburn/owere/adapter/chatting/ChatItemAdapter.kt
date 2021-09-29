package com.blooburn.owere.adapter.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.databinding.ItemChatBinding
import com.blooburn.owere.item.ChatItem

class ChatItemAdapter  : androidx.recyclerview.widget.ListAdapter<ChatItem, ChatItemAdapter.ViewHolder>(diffUtil) {
    //ListAdapter임포트 adroidx주의
    inner class ViewHolder(private val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatItem: ChatItem) {
            binding.senderTextView.text = chatItem.senderId
            binding.messageTextView.text = chatItem.message

        }
    }

    //어답터 인터페이스 implement
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //챗 아이템을 부모context에 그려주는 뷰홀더
        return ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }



    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }


        }
    }
}