package com.blooburn.owere.user.activity.main.userReservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.databinding.ActivityReserveMenuBinding
import com.blooburn.owere.user.adapter.userReservation.MenuSelectAdapter
import com.blooburn.owere.user.item.StyleMenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReserveMenuActivity : AppCompatActivity() {







    //메뉴선택 목록 레이아웃 바인딩
    private var binding: ActivityReserveMenuBinding? = null

    //메뉴리스트 리사이클러뷰를 위한 어답터
    private lateinit var menuSelectAdapterCut: MenuSelectAdapter
    private lateinit var menuSelectAdapterMagic: MenuSelectAdapter
    private lateinit var menuSelectAdapterPerm: MenuSelectAdapter
    private lateinit var menuSelectAdapterDyeing: MenuSelectAdapter


    //DB 사용할 레퍼런스 초기화 (designerPriceChart -> 디자이너 Id -> 메뉴들)
    val tempDesignerId = "designer0" //디자이너 아이디

    val cutDB =
        Firebase.database.reference.child("designerPriceChart").child(tempDesignerId).child("커트")
    val magicDB =
        Firebase.database.reference.child("designerPriceChart").child(tempDesignerId).child("매직")
    val dyeingDB =
        Firebase.database.reference.child("designerPriceChart").child(tempDesignerId).child("염색")
    val permDB =
        Firebase.database.reference.child("designerPriceChart").child(tempDesignerId).child("펌")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //바인딩 사용
        binding = ActivityReserveMenuBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        //커트 리사이클러뷰 어답터 설정
        //인터페이스 implement
        menuSelectAdapterCut = MenuSelectAdapter (object : CheckboxAddedListener {
            override fun addCheckBox(checkbox: CheckBox) {
                checkBoxlist.add(checkbox)
            }
        })

        binding?.reserveMenuCutContainer!!.adapter = menuSelectAdapterCut
        binding?.reserveMenuCutContainer?.layoutManager = LinearLayoutManager(this)
        menuSelectAdapterCut.clearList()//뷰가 생성될 때마다 클리어 해줌


        //매직 리사이클러뷰 어답터 설정
        menuSelectAdapterMagic = MenuSelectAdapter (object : CheckboxAddedListener {
            override fun addCheckBox(checkbox: CheckBox) {
                checkBoxlist.add(checkbox)
            }
        })

        binding?.reserveMenuMagicContainer!!.adapter = menuSelectAdapterMagic
        binding?.reserveMenuMagicContainer!!.layoutManager = LinearLayoutManager(this)
        menuSelectAdapterMagic.clearList()

        //펌 리사이클러뷰 어답터 설정
        menuSelectAdapterPerm = MenuSelectAdapter (object : CheckboxAddedListener {
            override fun addCheckBox(checkbox: CheckBox) {
                checkBoxlist.add(checkbox)
            }
        })

        binding?.reserveMenuPermContainer!!.adapter = menuSelectAdapterPerm
        binding?.reserveMenuPermContainer!!.layoutManager = LinearLayoutManager(this)
        menuSelectAdapterPerm.clearList()

        //염색
        menuSelectAdapterDyeing = MenuSelectAdapter (object : CheckboxAddedListener {
            override fun addCheckBox(checkbox: CheckBox) {
                checkBoxlist.add(checkbox)
            }
        })
        binding?.reserveMenuDyeingContainer!!.adapter = menuSelectAdapterDyeing
        binding?.reserveMenuDyeingContainer!!.layoutManager = LinearLayoutManager(this)
        menuSelectAdapterDyeing.clearList()


        //tempDesignerId 밑에 데이터 하나라도 변화하면(생성, 삭제)
        //DB 변화 리스너 ->한 번만 불러옴
        cutDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var menuIndex = 0 //인덱스
                snapshot.children.forEach {
                    val model = it.getValue(StyleMenuItem::class.java)
                    model ?: return
                    //DB에 변화가 생긴다면
//
                    menuSelectAdapterCut.addData(model)


                    menuIndex++//아이템 인덱스 증가




                }

                menuSelectAdapterCut.notifyDataSetChanged()//뷰 목록 업데이트
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        magicDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var menuIndex=0
                snapshot.children.forEach {
                    val model = it.getValue(StyleMenuItem::class.java)
                    model ?: return
                    //DB에 변화가 생긴다면
                    menuSelectAdapterMagic.addData(model) //목록 리스트 추가




                }


//                추가해주기
                menuSelectAdapterMagic.notifyDataSetChanged()//뷰 목록 업데이트
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        permDB.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var menuIndex = 0
                snapshot.children.forEach {
                    val model = it.getValue(StyleMenuItem::class.java)
                    model ?: return
                    //DB에 변화가 생긴다면
                    menuSelectAdapterPerm.addData(model) //목록 리스트 추가



                }


//                추가해주기
                menuSelectAdapterPerm.notifyDataSetChanged()//뷰 목록 업데이트
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        dyeingDB.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var menuIndex = 0
                snapshot.children.forEach {
                    val model = it.getValue(StyleMenuItem::class.java)
                    model ?: return
                    //DB에 변화가 생긴다면
                    menuSelectAdapterDyeing.addData(model) //채팅방 목록 리스트 추가



                }


//                추가해주기
                menuSelectAdapterDyeing.notifyDataSetChanged()//뷰 목록 업데이트
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }
}

//체크박스를 위한 인터페이스
interface CheckboxAddedListener {
    fun addCheckBox(checkbox: CheckBox){
    }
}
var checkBoxlist = mutableListOf<CheckBox>()





