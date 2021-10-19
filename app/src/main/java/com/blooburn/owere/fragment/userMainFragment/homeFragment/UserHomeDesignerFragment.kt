package com.blooburn.owere.fragment.userMainFragment.homeFragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.R
import com.blooburn.owere.adapter.userHome.DesignerListAdapter
import com.blooburn.owere.databinding.UserHomeDesignerFragmentBinding
import com.blooburn.owere.item.UserDesignerItem
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class UserHomeDesignerFragment : Fragment(R.layout.user_home_designer_fragment) {

    private val tempUserId = "Pz0XMXih1iXhVMaiUVG6NNTkq2C2"

    private var binding: UserHomeDesignerFragmentBinding? = null
    private val database = FirebaseDatabase.getInstance()
    private val favoriteDesignersReference =
        database.getReference("favoriteDesigners").child(tempUserId)

    lateinit var favoriteDesignerListAdapter: DesignerListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = UserHomeDesignerFragmentBinding.bind(view)

        favoriteDesignerListAdapter = DesignerListAdapter() // 즐겨찾는 디자이너 리스트 어댑터

        // 즐겨찾는 디자이너 리사클이클러 뷰 초기화
        binding?.favoriteDesignerRecyclerView?.apply{
            layoutManager = LinearLayoutManager(activity)
            adapter = favoriteDesignerListAdapter
        }

        // 즐겨찾는 디자이너 리스트 초기화, 파이어베이스와 연동
        initFavoriteDesignerList()
    }

    /**
     * 즐겨찾는 디자이너 리스트 초기화
     * 업데이트 or 화면 진입할 때마다 어댑터의 리스트에 접근해서 전체 삭제 이후, 하나씩 추가
     * 즐겨찾는 디자이너가 많지 않을테니 괜찮을 것 같다
     */
    private fun initFavoriteDesignerList() {
        favoriteDesignersReference.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                favoriteDesignerListAdapter.clearList()

                for(dataSnapshot in snapshot.children){
                    val designerData = dataSnapshot.getValue(UserDesignerItem::class.java)?.apply{
                        if (dataSnapshot.key != null){
                            designerId = dataSnapshot.key!! // 각 data의 key 값을 디자이너 아이디로 설정해두었습니다
                        }
                    }

                    // 디자이너 데이터 하나씩 추가
                    if (designerData != null){
                        favoriteDesignerListAdapter.addData(designerData)
                    }
                }

                favoriteDesignerListAdapter.notifyDataSetChanged()  // 전부 추가하고 나서 어댑터에 알림
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}