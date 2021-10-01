package com.blooburn.owere.fragment.userMainFragment.chattingFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.R
import com.blooburn.owere.activity.UserMain.chattingActivity.ChattingActivity
import com.blooburn.owere.adapter.chatting.ChatListAdapter
import com.blooburn.owere.databinding.LayoutChattingDesignerFragmentBinding
import com.blooburn.owere.item.ChatListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp
import kotlin.concurrent.timer

class ChattingDesignerFragment : Fragment(R.layout.layout_chatting_designer_fragment) {


    //고객 전용
    //채팅목록 레이아웃 바인딩
    private var binding : LayoutChattingDesignerFragmentBinding? = null
    //채팅리스트 리사이클러뷰를 위한 어답터
    private lateinit var chatListAdapter: ChatListAdapter
    //채팅방 목록
    private val chatRoomList = mutableListOf<ChatListItem>()


    private val auth : FirebaseAuth by lazy{
        Firebase.auth
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //바인딩 초기화
        val chattingDesignerFragmentBinding = LayoutChattingDesignerFragmentBinding.bind(view)
        binding = chattingDesignerFragmentBinding

        //리스트에서 채팅방 클릭시 해당 채팅방으로 이동
        chatListAdapter = ChatListAdapter(onItemClicked = {chatListItem ->
            //채팅방으로 이동하는 코드
            //context에 nullcheck를 걸어둔다
            context?.let{
                val intent = Intent(it, ChattingActivity::class.java)
                intent.putExtra("chatRoomId", chatListItem.chatRoomId)
                intent.putExtra("userName", chatListItem.myName.toString())
                startActivity(intent)
            }

        })

        binding?.chatListRecyclerView?.adapter = chatListAdapter
        binding?.chatListRecyclerView?.layoutManager =LinearLayoutManager(context)



        chatRoomList.clear()    //뷰가 생성될 때마다 클리어 해줌

        //로그인이 안 되어있다면 불러올 데이터 없음
        if(auth.currentUser == null){
            return
        }

       // chatRoomId = "@make@${auth.currentUser?.uid}@time@${System.currentTimeMillis()}"


        //DB 사용할 레퍼런스 초기화 (UserRooms -> 유저Id -> 채팅방ID)
        val chatDB = Firebase.database.reference.child("UserRooms").child(auth.currentUser!!.uid)

        //UserId 밑에 데이터 하나라도 변화하면(채팅방 생성, 삭제)
        //DB 변화 리스너 ->한 번만 불러옴
        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return
                    //DB에 변화가 생긴다면
                    chatRoomList.add(model) //채팅방 목록 리스트 추가
                }

                chatListAdapter.submitList(chatRoomList) //변화한 채팅방 목록 어답터에 제출
                chatListAdapter.notifyDataSetChanged()//뷰 목록 업데이트
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }


}