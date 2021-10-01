package com.blooburn.owere.adapter.chatting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.databinding.ItemChatListBinding
import com.blooburn.owere.item.ChatListItem
//채팅방 목록 리사이클러뷰를 위한 어답터
class ChatListAdapter(val onItemClicked : (ChatListItem)-> Unit) : ListAdapter<ChatListItem, ChatListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(chatListItem: ChatListItem) {

//            컴포넌트 클릭 이벤트
            binding.root.setOnClickListener{
                onItemClicked(chatListItem)
            }
            //채팅방 상대방을 타이틀로 설정
            //채팅방 유저 명단을 받아옴
            var chatTitle = chatListItem.opponentName
            //마지막 메시지
            var chatLastMessage = chatListItem.lastMessage



            //채팅방 타이틀에 상대방이름을 표시
            binding.chatRoomTitleTextView.text = chatTitle
            binding.chattinglistLastmessage.text = chatLastMessage
//            프로필 표시 필요
//            binding.chattinglistProfile
//            시간 표시 필요


        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemChatListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(currentList[position])
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatListItem>() {
            override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}