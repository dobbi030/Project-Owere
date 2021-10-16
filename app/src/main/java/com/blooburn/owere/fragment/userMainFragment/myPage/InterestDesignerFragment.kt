package com.blooburn.owere.fragment.userMainFragment.myPage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.R
import com.blooburn.owere.adapter.myPage.InterestDesignerAdpater
import com.blooburn.owere.databinding.LayoutChattingDesignerFragmentBinding
import com.blooburn.owere.databinding.LayoutInterestDesignerFragmentBinding
import com.blooburn.owere.item.ChatListItem
import com.blooburn.owere.item.FavoriteDesigner
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InterestDesignerFragment : Fragment(R.layout.layout_interest_designer_fragment) {

    // 관심 디자이너 레이아웃 바인딩
    private var binding : LayoutInterestDesignerFragmentBinding? = null
    //디자이너 리스트 리사이클러뷰를 위한 어답터

    private lateinit var interestDesignerAdpater : InterestDesignerAdpater
    //관심 디자이너 목록

    private var favoriteDesignerList = mutableListOf<FavoriteDesigner>()

    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //바인딩 초기화
        val interestDesignerFragmentBinding = LayoutInterestDesignerFragmentBinding.bind(view)
        binding = interestDesignerFragmentBinding

        interestDesignerAdpater = InterestDesignerAdpater()



        binding?.favoriteDesignerListRecyclerView?.adapter = interestDesignerAdpater
        binding?.favoriteDesignerListRecyclerView?.layoutManager = LinearLayoutManager(context)

        favoriteDesignerList.clear()    //뷰가 생성될 때마다 클리어 해줌


        if(auth.currentUser == null){
            return
        }

        //DB 사용할 레퍼런스 초기화 (favoriteDesigners -> 유저Id -> 디자이너)
        val chatDB = Firebase.database.reference.child("favoriteDesigners").child(auth.currentUser!!.uid)

        //UserId 밑에 데이터 하나라도 변화하면(채팅방 생성, 삭제)
        //DB 변화 리스너 ->한 번만 불러옴
        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val model = it.getValue(FavoriteDesigner::class.java)
                    model ?: return
                    //DB에 변화가 생긴다면
                    favoriteDesignerList.add(model) // 목록 리스트 추가
                }

                interestDesignerAdpater.submitList(favoriteDesignerList) //변화한 목록 어답터에 제출
                interestDesignerAdpater.notifyDataSetChanged()//뷰 목록 업데이트
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }



}
