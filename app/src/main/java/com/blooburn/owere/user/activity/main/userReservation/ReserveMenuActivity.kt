package com.blooburn.owere.user.activity.main.userReservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CheckBox
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.databinding.ActivityReserveMenuBinding

import com.blooburn.owere.user.adapter.userReservation.MenuSelectAdapter
import com.blooburn.owere.user.fragment.mainFragment.reservationFragment.MenuBottomDialogFragment
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserDesignerItem
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class ReserveMenuActivity : AppCompatActivity() {

    private var designerData: UserDesignerItem? = null//프로필에서 전달받을 디자이너 객체

    //길이추가 커스텀 다이얼로그
    val menuBottomDialogFragment: MenuBottomDialogFragment? = MenuBottomDialogFragment()

    //메뉴선택 목록 레이아웃 바인딩
    private var binding: ActivityReserveMenuBinding? = null

    //메뉴리스트 리사이클러뷰를 위한 어답터
    private lateinit var menuSelectAdapterCut: MenuSelectAdapter
    private lateinit var menuSelectAdapterMagic: MenuSelectAdapter
    private lateinit var menuSelectAdapterPerm: MenuSelectAdapter
    private lateinit var menuSelectAdapterDyeing: MenuSelectAdapter

    private val databaseReference = databaseInstance.reference

    //디자이너 아이디
    var designerId : String?=null



    //각 리사이클러뷰 데이터를 가져올 DB레퍼런스
    var cutDB : DatabaseReference?= null
    var magicDB :DatabaseReference?= null

    var dyeingDB :DatabaseReference?= null

    var permDB  :DatabaseReference?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //바인딩 사용
        binding = ActivityReserveMenuBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        /**
         * 수신 인텐트로 전달받은 디자이너 정보 저장
         */
        getDesignerDataFromIntent()

        /**
         * 리사이클러뷰 어답터 설정
         */
        initAdapter()
        /**
         * 디자이너 아이디로 DB Reference 설정
         */
        setDataReferences()


        /**
         * 선택완료 버튼 리스너
         */
        initButton()



        /**
         * 디자이너 메뉴 목록 불러와서 적용
         */
        initMenu()


    }

    /**
     * 수신 인텐트로 전달받은 디자이너 정보 저장
     */
    private fun getDesignerDataFromIntent() {
        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }

        designerData = extras!!.getParcelable(DESIGNER_DATA_KEY)   // DesignerData 객체 읽기
        if (designerData == null) {
            finish()
        }
    }

    /**
     * 리사이클러뷰 어답터 설정
     */
    private fun initAdapter() {
        //커트 리사이클러뷰 어답터 설정
        //인터페이스 implement
        menuSelectAdapterCut = MenuSelectAdapter(object : CheckboxAddedListener {
            override fun addCheckBox(checkbox: CheckBox) {
                checkBoxlist.add(checkbox)
            }
        })

        binding?.reserveMenuCutContainer!!.adapter = menuSelectAdapterCut
        binding?.reserveMenuCutContainer?.layoutManager = LinearLayoutManager(this)
        menuSelectAdapterCut.clearList()//뷰가 생성될 때마다 클리어 해줌

        //매직 리사이클러뷰 어답터 설정
        menuSelectAdapterMagic = MenuSelectAdapter(object : CheckboxAddedListener {
            override fun addCheckBox(checkbox: CheckBox) {
                checkBoxlist.add(checkbox)
            }
        })

        binding?.reserveMenuMagicContainer!!.adapter = menuSelectAdapterMagic
        binding?.reserveMenuMagicContainer!!.layoutManager = LinearLayoutManager(this)
        menuSelectAdapterMagic.clearList()

        //펌 리사이클러뷰 어답터 설정
        menuSelectAdapterPerm = MenuSelectAdapter(object : CheckboxAddedListener {
            override fun addCheckBox(checkbox: CheckBox) {
                checkBoxlist.add(checkbox)
            }
        })

        binding?.reserveMenuPermContainer!!.adapter = menuSelectAdapterPerm
        binding?.reserveMenuPermContainer!!.layoutManager = LinearLayoutManager(this)
        menuSelectAdapterPerm.clearList()

        //염색
        menuSelectAdapterDyeing = MenuSelectAdapter(object : CheckboxAddedListener {
            override fun addCheckBox(checkbox: CheckBox) {
                checkBoxlist.add(checkbox)
            }
        })
        binding?.reserveMenuDyeingContainer!!.adapter = menuSelectAdapterDyeing
        binding?.reserveMenuDyeingContainer!!.layoutManager = LinearLayoutManager(this)
        menuSelectAdapterDyeing.clearList()


    }

    /**
     * 메뉴 데이터를 어답터에 업데이트
     */
    private fun getAndSetMenu(menuReference: DatabaseReference?, Adapter : MenuSelectAdapter) {
        menuReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(StyleMenuItem::class.java)
                    model ?: return
                    //DB에 변화가 생긴다면
//
                    Adapter.addData(model)

                }

                Adapter.notifyDataSetChanged()//뷰 목록 업데이트
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /**
     * 메뉴 뷰 초기화
     */
    private fun initMenu() {
        //tempDesignerId 밑에 데이터 하나라도 변화하면(생성, 삭제)
        //DB 변화 리스너 ->한 번만 불러옴

        /**
         * 메뉴 데이터를 어답터에 업데이트
         */
        getAndSetMenu(cutDB,menuSelectAdapterCut)
        getAndSetMenu(magicDB,menuSelectAdapterMagic)
        getAndSetMenu(permDB,menuSelectAdapterPerm)
        getAndSetMenu(dyeingDB,menuSelectAdapterDyeing)


    }

    /**
     * 디자이너 아이디로 DB Reference 설정
     */
    private fun setDataReferences() {
        // 디자이너의 데이터 참조
        //DB 사용할 레퍼런스 초기화 (designerPriceChart -> 디자이너 Id -> 메뉴들)
        designerId = designerData?.designerId.toString();

//        "designer0" //디자이너 아이디

        cutDB = databaseReference.child("designerPriceChart").child(designerId!!).child("커트")
        magicDB = databaseReference.child("designerPriceChart").child(designerId!!).child("매직")
        dyeingDB = databaseReference.child("designerPriceChart").child(designerId!!).child("염색")
        permDB = databaseReference.child("designerPriceChart").child(designerId!!).child("펌")
    }
    private fun initButton(){
        binding?.menuChoiceCompleteButton?.setOnClickListener {
            //선택된 메뉴 객체 확인


            if(selectedMenu==null){
                Toast.makeText(this,"메뉴를 선택해주세요", Toast.LENGTH_SHORT).show()
            }else{
                if(selectedMenu?.addLength==1){
                    //선택완료 시 커스텀 다이얼로그 띄워주기
                    menuBottomDialogFragment?.show(supportFragmentManager, menuBottomDialogFragment.tag)

                    //프래그먼트로 객체 전달
                    val bundle = bundleOf() //어떤 키에 어떤 값으로 번들을 담겠다

                    //디자이너 정보 전송
                    bundle.putParcelable(DESIGNER_DATA_KEY,designerData)
                    //선택한 메뉴 전송
                    bundle.putParcelable("SESLECTED_MENU_DATA_KEY", selectedMenu)

                    menuBottomDialogFragment?.arguments = bundle




                }else{
                    //길이 추가 상관없는 메뉴 선택시 샵 선택 액티비티로 전환
                    val intent = Intent(this, ShopsOfDesignerActivity::class.java)
                    intent.putExtra(DESIGNER_DATA_KEY,designerData)
                    startActivity(intent)

                }
                checkBoxlist.clear()
                selectedMenu = null
            }

        }

    }

}

//체크박스를 위한 인터페이스
interface CheckboxAddedListener {
    fun addCheckBox(checkbox: CheckBox) {
    }
}

var checkBoxlist = mutableListOf<CheckBox>()
//전달 해줄 객체
var selectedMenu : StyleMenuItem? = null