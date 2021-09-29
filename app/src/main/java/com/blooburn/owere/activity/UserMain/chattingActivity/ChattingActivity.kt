package com.blooburn.owere.activity.UserMain.chattingActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.adapter.chatting.ChatItemAdapter
import com.blooburn.owere.item.ChatItem
import com.blooburn.owere.item.DatabaseChild.Companion.DB_CHAT
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//채팅리스트가 실시간으로 올라감. 원래는 웹소켓을 사용하거나 retrofit에서 항상 http를 가져오는 방식으로 풀링방식으로 10초에 한 번씩 확인하거나...까다로움
//firebase Realtime DB를 활용해서 구현

class ChattingActivity : AppCompatActivity() {

    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    private val chatList = mutableListOf<ChatItem>()

    private val adapter = ChatItemAdapter()

    private var chatDB : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        val chatRoomId = intent.getLongExtra("chatRoomId", -1)

        chatDB = Firebase.database.reference.child(DB_CHAT).child("$chatRoomId")

        //DB갱신할 때마다  실시간으로 읽어옴
        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                adapter.submitList(chatList)
                adapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        findViewById<RecyclerView>(R.id.chatRecyclerView).adapter = adapter
        findViewById<RecyclerView>(R.id.chatRecyclerView).layoutManager = LinearLayoutManager(this)

       /* data class ChatItem(
            val profileImg: String,
            val message: String,
            val timestamp: String,
            val uid: String,
            val userName: String
        )
        */
        findViewById<Button>(R.id.sendButton).setOnClickListener {
            val chatItem = ChatItem(profileImg = "userprofile",
                message = findViewById<EditText>(R.id.messageEditText).text.toString(),
                timestamp = System.currentTimeMillis().toString(),
                uid = auth.currentUser?.uid.toString(),
                userName = "myName"
            )

            chatDB?.push()?.setValue(chatItem)
        }




    }
}