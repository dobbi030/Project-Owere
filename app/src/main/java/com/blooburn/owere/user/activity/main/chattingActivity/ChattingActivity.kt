package com.blooburn.owere.user.activity.main.chattingActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.user.adapter.chatting.ChatItemAdapter
import com.blooburn.owere.user.item.ChatItem
import com.blooburn.owere.user.item.DatabaseChild.Companion.DB_CHAT
import com.blooburn.owere.user.item.DesignerItem
import com.blooburn.owere.user.item.UserEntity
import com.blooburn.owere.user.item.UserReview
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

//채팅리스트가 실시간으로 올라감. 원래는 웹소켓을 사용하거나 retrofit에서 항상 http를 가져오는 방식으로 풀링방식으로 10초에 한 번씩 확인하거나...까다로움
//firebase Realtime DB를 활용해서 구현

class ChattingActivity : AppCompatActivity() {

    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }


    //디자이너 프로필에서 전달받을 디자이너 객체
    private var designerData: DesignerItem? = null

    private var chatList = mutableListOf<ChatItem>()

    //리사이클러뷰 어답터
    private val adapter = ChatItemAdapter()

    //파이어베이스 데이터베이스 레퍼런스를 받을 변수
    private var chatDB : DatabaseReference? = null

    private val currentUserDB = databaseInstance.reference.child("Users")
    private val UserRoomsDB = databaseInstance.reference.child("UserRooms")
    //채팅 내용이 들어갈 리사이클러뷰
    private val chatRecyclerview by lazy {
        findViewById<RecyclerView>(R.id.chatRecyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting)

        getDataFromIntent()


        //채팅방 목록 프래그먼트로부터 받아온 정보
        //채팅방 아이디 @User@${userId}@UserId@$디자이너ID
        val chatRoomId = intent.getStringExtra("chatRoomId")
        val designerName =  intent.getStringExtra("designerName")
        var designerId =intent.getStringExtra("designerId")
        var designerProfile = intent.getStringExtra("designerProfile")
        var userName : String? = intent.getStringExtra("userName")


        if (userName == null){
            var userEntity : UserEntity?
            //유저 네임을 DB에서 가져옴
        currentUserDB.child(auth.currentUser!!.uid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userEntity = snapshot.getValue(UserEntity::class.java)
                userName = userEntity!!.myName



            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        }



        chatDB = Firebase.database.reference.child(DB_CHAT).child("$chatRoomId")


        //DB갱신할 때마다  실시간으로 읽어옴
        chatDB?.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatItem = snapshot.getValue(ChatItem::class.java)
                chatItem ?: return

                chatList.add(chatItem)
                adapter.datas = chatList
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
            val chatItem =
                ChatItem(profileImg = "userprofile",
                message = findViewById<EditText>(R.id.messageEditText).text.toString(),
                timestamp = System.currentTimeMillis().toString(),
                uid = auth.currentUser?.uid.toString(),
                //유저 이름 인텐트로 받아오기 필요
                userName = userName!!
            )
            //UserRooms 업데이트
            val userRoomsStatus = mutableMapOf<String, Any>()
            userRoomsStatus["chatRoomId"] = chatRoomId!!
            userRoomsStatus["lastMessage"] = findViewById<EditText>(R.id.messageEditText).text.toString()
            userRoomsStatus["myName"] = userName!!
            userRoomsStatus["opponentId"] = designerData?.designerId.toString()
            userRoomsStatus["opponentName"] = designerData?.name.toString()
            userRoomsStatus["profileImg"] = designerData?.profileImagePath.toString()
            UserRoomsDB.child(auth.currentUser!!.uid).child(chatRoomId!!).updateChildren(userRoomsStatus)


            val designerRoomsStatus = mutableMapOf<String, Any>()
            designerRoomsStatus["chatRoomId"] = chatRoomId!!
            designerRoomsStatus["lastMessage"] = findViewById<EditText>(R.id.messageEditText).text.toString()
            designerRoomsStatus["myName"] = designerData?.name.toString()
            designerRoomsStatus["opponentId"] = auth.currentUser!!.uid
            designerRoomsStatus["opponentName"] = userName.toString()
            designerRoomsStatus["profileImg"] = ""

            UserRoomsDB.child(designerData!!.designerId).child(chatRoomId!!).updateChildren(designerRoomsStatus)

            chatRecyclerview.scrollToPosition(chatList.size-1)







            val messageID = "${chatItem.timestamp}+${chatItem.uid}"
            //DB 갱신
            chatDB?.push()?.setValue(chatItem)

            //보낸 후 텍스트 초기화
            findViewById<EditText>(R.id.messageEditText).text.clear()
        }
        initGoBackButton()




    }

    private fun initGoBackButton() {
        var goBackButton = findViewById<ImageView>(R.id.user_chatting_activity_go_back_button)
        goBackButton.setOnClickListener {
            finish()
        }
    }

    /**
     * 수신 인텐트로 전달받은 디자이너 정보 저장
     */
    private fun getDataFromIntent() {

        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }


        designerData = extras?.getParcelable(DESIGNER_DATA_KEY) // DesignerData 객체 읽기
        if(designerData?.name==null){
            val designerName =  intent.getStringExtra("designerName")
            var designerId =intent.getStringExtra("designerId")
            var designerProfile = intent.getStringExtra("designerProfile")

            designerData = DesignerItem(designerId!!,"", "", 0, designerName!!,
                designerProfile!!, 0.0, 0,"","",
                mutableListOf<String>(), ArrayList<UserReview>()
            )
        }



    }
}