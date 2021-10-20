package com.blooburn.owere.user.fragment.mainFragment.myPage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.R
import com.blooburn.owere.user.adapter.myPage.InterestShopAdpater
import com.blooburn.owere.databinding.LayoutInterestShopFragmentBinding
import com.blooburn.owere.user.item.FavoriteShop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class InterestShopFragment : Fragment(R.layout.layout_interest_shop_fragment) {
    // 관심 미용실 레이아웃 바인딩
    private var binding : LayoutInterestShopFragmentBinding? = null
    //미용실 리스트 리사이클러뷰를 위한 어답터

    private lateinit var interestShopAdpater : InterestShopAdpater
    //관심 미용실 목록

    private var favoriteShopList = mutableListOf<FavoriteShop>()

    private val auth : FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //바인딩 초기화
        val interestShopFragmentBinding = LayoutInterestShopFragmentBinding.bind(view)
        binding = interestShopFragmentBinding

        interestShopAdpater = InterestShopAdpater()



        binding?.favoriteShopListRecyclerView?.adapter = interestShopAdpater
        binding?.favoriteShopListRecyclerView?.layoutManager = LinearLayoutManager(context)

        favoriteShopList.clear()    //뷰가 생성될 때마다 클리어 해줌


        if(auth.currentUser == null){
            return
        }

        //DB 사용할 레퍼런스 초기화 (favoriteDesigners -> 유저Id -> 미용실)
        val chatDB = Firebase.database.reference.child("favoriteShop").child(auth.currentUser!!.uid)

        //UserId 밑에 데이터 하나라도 변화하면(채팅방 생성, 삭제)
        //DB 변화 리스너 ->한 번만 불러옴
        chatDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{
                    val model = it.getValue(FavoriteShop::class.java)
                    model ?: return
                    //DB에 변화가 생긴다면
                    favoriteShopList.add(model) // 목록 리스트 추가
                }

                interestShopAdpater.submitList(favoriteShopList) //변화한 목록 어답터에 제출
                interestShopAdpater.notifyDataSetChanged()//뷰 목록 업데이트
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}