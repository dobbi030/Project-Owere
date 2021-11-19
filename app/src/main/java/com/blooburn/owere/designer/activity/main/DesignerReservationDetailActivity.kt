package com.blooburn.owere.designer.activity.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.designer.item.DesignerReservationDetail
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.AllPricesFragment
import com.blooburn.owere.user.item.MenuItem
import com.blooburn.owere.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

interface DetailMenuItemHandler{
    val menuItemList: MutableList<MenuItem>
    fun onMenuChangedListener(changedList: MutableList<MenuItem>)
}

class DesignerReservationDetailActivity : AppCompatActivity(), DetailMenuItemHandler {

    private var reservation: DesignerReservationDetail? = null
    private val tempDesignerId = "designer0"
    private var userId = ""
    private var dateStamp = 0L  // 송신 인텐트에서 건네 받을 값
    private val milliSecondsPerDay = 86400000 // 24시간에 해당하는 밀리세컨드

    private val priceTxtView: TextView by lazy {
        findViewById(R.id.text_designer_reservation_detail_price)
    }
    private val menuTxtView: TextView by lazy {
        findViewById(R.id.text_designer_reservation_detail_menu)
    }
    private val totalPriceTxtView: TextView by lazy {
        findViewById(R.id.text_designer_reservation_detail_total_price)
    }
    private val paymentTxtView: TextView by lazy {
        findViewById(R.id.text_designer_reservation_detail_final_payment)
    }

    override val menuItemList = mutableListOf<MenuItem>()
    override fun onMenuChangedListener(changedList: MutableList<MenuItem>) {
        val menuList = mutableListOf<String>()
        val priceList = mutableListOf<Int>()

        changedList.forEach { menuItem ->
            menuList.add(menuItem.menuName)
            priceList.add(Integer.parseInt(menuItem.menuPrice))
        }

        initMenuAndPriceTextView(menuList, priceList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designer_reservation_detail)

        getUserIdFromIntent()
        getReservationFromDBAndInitViews()
        convertDateStampToString()
    }

    /**
     * 수신 인텐트로 전달받은 예약한 유저의 아이디 저장
     */
    private fun getUserIdFromIntent() {
        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }

        userId = extras?.getString(UID_KEY) ?: ""
        dateStamp = extras?.getLong(DATE_STAMP_KEY) ?: 0
        if (userId == "" || dateStamp == 0L) {
            finish()
        }
    }

    private fun getReservationFromDBAndInitViews() {
        val referencePath = "designerReservations/$tempDesignerId/$dateStamp/$userId"
        databaseInstance.reference.child(referencePath).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                reservation = snapshot.getValue(DesignerReservationDetail::class.java)
                if (reservation == null) {
                    finish()
                }

                initBottomButtons()
                initUI()

                /* 파이어베이스에서 리스트 읽는 방법2

                val genericTypeIndicator = object : GenericTypeIndicator<List<String>>() {}
                val listOfMenu = snapshot.child("menuList").getValue(genericTypeIndicator)
                Log.d("listOfMenu", "$listOfMenu")
                */
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun initUI() {
        if (reservation == null) return

        findViewById<TextView>(R.id.text_designer_reservation_detail_dates).text =
            convertDateStampToString()  // 날짜
        findViewById<TextView>(R.id.text_designer_reservation_detail_time).apply {
            text = getTreatmentTime(this, reservation as DesignerReservation)
        }   // 시간
        findViewById<TextView>(R.id.text_designer_reservation_detail_designer).text =
            reservation!!.designerName  // 디자이너
        findViewById<TextView>(R.id.text_designer_reservation_detail_shop).text =
            reservation!!.shop  // 미용실

        // 메뉴 종류, 메뉴 가격, 상품 합계, 결제 금액 텍스트 초기화
        initMenuAndPriceTextView(reservation!!.menuList, reservation!!.priceList)

    }

    /**
     * 아래 정산관련 버튼들 초기화
     * 1. visibility
     * 2. click listener
     */
    private fun initBottomButtons() {
        if (reservation!!.type == TypeOfReservation.TREATED.value){
            setBottomButtonsVisible()
            setAdditionalTreatmentClickListener()
        }
    }

    /**
     * 정산할 예약일 때만 아래에 정산 관련 버튼들이 보이도록 한다
     */
    private fun setBottomButtonsVisible() {
        findViewById<LinearLayout>(R.id.layout_designer_reservation_detail_buttons).visibility =
            View.VISIBLE
    }

    /**
     * 추가시술 버튼 클릭 리스너
     */
    private fun setAdditionalTreatmentClickListener() {
        val buttonView =
            findViewById<Button>(R.id.button_designer_reservation_detail_additional_treatment)

        buttonView.setOnClickListener {
            val fragment = AllPricesFragment(isAdditionTreatment = true)

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_designer_reservation_detail, fragment)
                .addToBackStack(ALL_PRICES_FRAGMENT_NAME)
                .commitAllowingStateLoss()
        }
    }

    /**
     * 날짜스탬프를 문자열로 변환
     */
    private fun convertDateStampToString(): String {
        val formatter = SimpleDateFormat("MMM d일 (E)", Locale.KOREA)
        Log.d("날짜", formatter.format(dateStamp * milliSecondsPerDay))

        return formatter.format(dateStamp)
    }

    /**
     * 타임스탬프를 문자열로 변환
     */
    private fun convertMilliSecondsToTimeString(milliSeconds: Long): String {
        val formatter = SimpleDateFormat("a kk:mm", Locale.KOREA).apply {
            timeZone = TimeZone.getTimeZone("KST")
        }

        return formatter.format(milliSeconds)
    }

    /**
     * 예약된 시술 시간 "시작 ~ 끝" 문자열로 변환
     */
    private fun getTreatmentTime(itemView: View, reservation: DesignerReservation): String {
        return itemView.context.getString(
            R.string.reservation_time,
            convertMilliSecondsToTimeString(reservation.startTime),
            convertMilliSecondsToTimeString(reservation.endTime)
        )
    }

    private fun initMenuAndPriceTextView(menuList: List<String>, priceList: List<Int>){
        menuTxtView.text = getStringOfMenuFrom(menuList) // 메뉴 종류

        getStringsOfPriceFrom(priceList).let { (menuPrices, totalPrice) ->
            priceTxtView.text = menuPrices  // 메뉴 가격
            totalPriceTxtView.text = totalPrice  // 상품 합계
            paymentTxtView.text = totalPrice  // 결제 금액 --> 당장은 상품 합계 = 결제 금액
        }
    }

    /**
     * 메뉴 리스트 -> 메뉴 종류 텍스트로 변환
     */
    private fun getStringOfMenuFrom(menuList: List<String?>): String {
        return menuList.filterNotNull().reduce { acc, menu -> "$acc + $menu" }
    }

    /**
     * 가격 리스트 -> 1. 메뉴 가격 텍스트, 2. 상품 합계 텍스트
     */
    private fun getStringsOfPriceFrom(priceList: List<Int?>): Pair<String, String> {
        Log.d("로그", "$priceList")
        val decimalFormat = DecimalFormat("#,###원")
        var totalPrice = 0

        return Pair(priceList
            .filterNotNull()
            .map { price ->
                totalPrice += price
                decimalFormat.format(price)
            }
            .reduce { acc, price -> "$acc + $price" }, decimalFormat.format(totalPrice)
        )
    }
}