package com.blooburn.owere.user.fragment.mainFragment.homeFragment

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.R
import com.blooburn.owere.user.adapter.home.DesignerListAdapter
import com.blooburn.owere.databinding.UserHomeDesignerFragmentBinding
import com.blooburn.owere.user.item.DesignerItem
import com.blooburn.owere.user.item.LocationClass
import com.blooburn.owere.user.item.UserDesignerItem
import com.blooburn.owere.user.item.UserEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Math.*
import java.util.*
import kotlin.math.pow

//유저 홈 화면 (근처의 예약 가능한 디자이너, 즐겨찾는 디자이너)
class UserHomeDesignerFragment : Fragment(R.layout.user_home_designer_fragment) {

    private val tempUserId = "Pz0XMXih1iXhVMaiUVG6NNTkq2C2"

    private var binding: UserHomeDesignerFragmentBinding? = null
    private val database = FirebaseDatabase.getInstance()

    private val favoriteDesignersReference =
        database.getReference("favoriteDesigners").child(tempUserId)

    //모든 디자이너 목록 조회를 위한 DB레퍼런스 , 디자이너가 많아지면 수정
    private val locationDesignerReference =
        database.getReference("LocationDesigner")

    private val designerInfoReference =
        database.getReference("Designers")

    lateinit var favoriteDesignerListAdapter: DesignerListAdapter
    lateinit var availableDesignerListAdapter : DesignerListAdapter

    //유저의 좌표
    private var latitude : Double = 0.0
    private var longitude : Double= 0.0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = UserHomeDesignerFragmentBinding.bind(view)

        favoriteDesignerListAdapter = DesignerListAdapter() //즐겨찾는 디자이너 리스트 어댑터

        // 즐겨찾는 디자이너 리사클이클러 뷰 초기화
        binding?.favoriteDesignerRecyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = favoriteDesignerListAdapter
        }

        // 즐겨찾는 디자이너 리스트 초기화, 파이어베이스와 연동
        initFavoriteDesignerList()

        availableDesignerListAdapter = DesignerListAdapter() //지금 가능한 디자이너 리스트 어댑터

        // 이용 가능한 디자이너 리사클이클러 뷰 초기화
        binding?.availableDesignerRecyclerView?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = availableDesignerListAdapter

        }

        //유저의 현재 좌표 정보를 가져옴
        MyLocationSetting()

        //이용가능한 디자이너 리스트 초기화
        initAvailableDesignerList()

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
                    val designerData = dataSnapshot.getValue(DesignerItem::class.java)?.apply{
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

            }
        })
    }

    //유저DB 레퍼런스
    private val myLocationReference =
        database.getReference("Users").child(tempUserId)
    /**
     * 현재 유저의 좌표 조회
     */

    private fun MyLocationSetting(){
        myLocationReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                    val model = snapshot.getValue(UserEntity::class.java)
                    latitude = model!!.latitude
                    longitude = model!!.longitude



            }

            override fun onCancelled(error: DatabaseError) {



            }

        })
    }
    /**
     * 이용가능한 디자이너 리스트 초기화
     */
   // 이용가능 디자이너 리스트
    private var availableDesignerList = mutableListOf<String>()
    private var allDesignerList = mutableMapOf<String,UserDesignerItem>()

    private fun initAvailableDesignerList(){


        //가입되어 있는 디자이너들의 좌표들을 조회
        locationDesignerReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                availableDesignerListAdapter.clearList()


                snapshot.children.forEach { dataSnapshot ->
                    var model = dataSnapshot.getValue(LocationClass::class.java)
                    val designerLatitude = model?.latitude
                    val designerLongitude = model?.longitude
                    // 디자이너와 유저의 거리계산
                    val distance = DistanceManager.getDistance(latitude,longitude,designerLatitude!!,designerLongitude!!)


                    val designerId = dataSnapshot.key
                    Log.d("designer","desiger.key is $designerId")
                    if(distance<=5000) {//5km 이내라면 이용가능 디자이너 리스트에 추가
                        //이용가능 디자이너리스트에 id 추가
                        availableDesignerList.add(designerId!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        designerInfoReference.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               snapshot.children.forEach {dataSnapshot ->
                   val model = dataSnapshot.getValue(UserDesignerItem::class.java)
                   val designerId = dataSnapshot.key!!
                   if(model!=null){
                       //전체 디자이너 리스트에 추가
                       allDesignerList.put(designerId,model)
                       Log.d("designer","AddOnDesigner is $designerId")
                   }
               }

                //이용가능한 디자이너 리스트를 모두 어답터에 넘겨줌
                for(i in 0..availableDesignerList.size-1){
                    val designerId = availableDesignerList[i]
                    Log.d("designer","available designer is $designerId")
                    val designer = allDesignerList.get(designerId)
                    availableDesignerListAdapter.addData(designer!!)
                }
                availableDesignerListAdapter.notifyDataSetChanged()  // 전부 추가하고 나서 어댑터에 알림

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })






    }

    /**
     * 두 좌표의 거리를 계산한다.
     *
     * @param lat1 위도1
     * @param lon1 경도1
     * @param lat2 위도2
     * @param lon2 경도2
     * @return 두 좌표의 거리(m)
     */

    object DistanceManager {

        private const val R = 6372.8 * 1000


        fun getDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Int {
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = sin(dLat / 2).pow(2.0) + sin(dLon / 2).pow(2.0) * cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
            val c = 2 * asin(sqrt(a))
            return (R * c).toInt()
        }
    }
}

