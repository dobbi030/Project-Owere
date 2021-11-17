package com.blooburn.owere.designer.activity.main

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.R
import com.blooburn.owere.designer.item.DesignerReservationDetail
import com.blooburn.owere.user.fragment.mainFragment.homeFragment.AllPricesFragment
import com.blooburn.owere.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class DesignerReservationDetailActivity : AppCompatActivity() {

    private var reservation: DesignerReservationDetail? = null
    private val tempDesignerId = "designer0"
    private var userId = ""
    private var dateStamp = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designer_reservation_detail)

        getUserIdFromIntent()
        getReservationFromDBAndInitBottomButtons()
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

    private fun getReservationFromDBAndInitBottomButtons(){
        val referencePath = "designerReservations/$tempDesignerId/$dateStamp/$userId"
        databaseInstance.reference.child(referencePath).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reservation = snapshot.getValue(DesignerReservationDetail::class.java)
                if (reservation == null) {
                    finish()
                }

                // initBottomButtons() // TODO 타입 받아오는 문제 해결

                /* 파이어베이스에서 리스트 읽는 방법2

                val genericTypeIndicator = object : GenericTypeIndicator<List<String>>() {}
                val listOfMenu = snapshot.child("menuList").getValue(genericTypeIndicator)
                Log.d("listOfMenu", "$listOfMenu")
                */
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    /**
     * 아래 정산관련 버튼들 초기화
     * 1. visibility
     * 2. click listener
     */
    private fun initBottomButtons() {
        if (reservation!!.type == TypeOfDesignerReservation.COMPLETED) {
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
}