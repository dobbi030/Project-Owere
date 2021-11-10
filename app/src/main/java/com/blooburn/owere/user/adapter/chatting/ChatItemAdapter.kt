package com.blooburn.owere.user.adapter.chatting


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.user.item.ChatItem
import com.google.firebase.auth.FirebaseAuth

//채팅창 메시지를 나열하는 리사이클러뷰 어답터
class ChatItemAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //현재 이용하는 유저의 정보를 불러오기 위한 Firebase 인스턴스
    private val mProfileUid = FirebaseAuth.getInstance()
    var datas = mutableListOf<ChatItem>() //메시지를 담을 배열


    //뷰 홀더 (상대방메시지)
    inner class ViewHolderOpMessage(view: View) :
        RecyclerView.ViewHolder(view) {
        var senderMessageText = view.findViewById<TextView>(R.id.senderTextView)
        var messageTextView = view.findViewById<TextView>(R.id.messageTextView)

        fun bind(chatItem: ChatItem) {
            senderMessageText.text = chatItem.userName
            messageTextView.text = chatItem.message

        }
    }
    //뷰 홀더(나의 메시지)
    inner class ViewHolderMyMessage(view: View) :
        RecyclerView.ViewHolder(view) {

        var myMessageText = view.findViewById<TextView>(R.id.mymessageTextView)

        fun bind(chatItem: ChatItem) {

            myMessageText.text = chatItem.message
        }
    }


    //어답터 인터페이스 implement//viewType에 의해 뷰를 구분(함수 호출 전에 getItemViewType에서 리턴)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View?

//        챗 아이템을 부모context에 그려주는 뷰홀더
        when (viewType) {    //return은 when에 붙여줘야함
//            내 메시지
            0 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_mychat, parent, false)


                return ViewHolderMyMessage(view)

            }

            else -> {// 상대 메시지
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_opponent, parent, false)

                return ViewHolderOpMessage(view)

//                ViewHolderOpMessage(
//                    ItemChatOpponentBinding.inflate(
//                        LayoutInflater.from(parent.context),
//                        parent,
//                        false
//                    )
//                )

            }

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //내 메시지인 경우

        holder.setIsRecyclable(false)
        if (datas[position].uid==mProfileUid.uid) {
            var holder0: ViewHolderMyMessage = holder as ViewHolderMyMessage
            holder0.bind(datas[position])
            holder0.setIsRecyclable(false)

        } else {



            var holder1: ViewHolderOpMessage = holder as ViewHolderOpMessage
            holder1.bind(datas[position])
            holder1.setIsRecyclable(false)
        }


    }

    //  내 메시지와 상대 메시지를 구분하여 각각 뷰를 두가지 분류로 리턴
    override fun getItemViewType(position: Int): Int {

        var chatMessage = datas[position]
        //메시지가 본인의 userId와 같다면 0, 다르면 1을 리턴하여 구분 onCreateViewHolder에서 사용
        if (chatMessage.uid==mProfileUid.uid) {
            return 0
        } else {
            return 1
        }


    }


//    companion object {
//        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
//            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
//                return oldItem.timestamp == newItem.timestamp
//            }
//
//            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
//                return oldItem == newItem
//            }
//
//
//        }
//    }

    override fun getItemCount(): Int {
        return datas.size
    }


}