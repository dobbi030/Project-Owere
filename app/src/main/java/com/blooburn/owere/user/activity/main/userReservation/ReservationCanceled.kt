package com.blooburn.owere.user.activity.main.userReservation


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.blooburn.owere.R
import com.blooburn.owere.user.item.ReservationListItem
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ReservationCanceled : AppCompatActivity() {

    private var reservation: ReservationListItem? = null //상세페이지에서 전달받을 예약 객체
    private var editTextReason : String? = null// 사유 입력 프래그먼트에서 전달받을 String

    private lateinit var auth: FirebaseAuth    //파이어베이스 인증 사용




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_canceled)

        //유저 정보를 접근하기위한 인증 인스턴스
        auth = Firebase.auth

        getDataFromIntent()

        //해당 날짜 유저 예약내역 레퍼런스
        val ReservationUserDB = databaseInstance.reference.child("UserReservation")
            .child(auth.currentUser!!.uid).child(reservation?.startTime.toString())

        //해당 날짜 디자이너 예약내역 레퍼런스
        val ReservationDesignerDB = databaseInstance.reference.child("designerReservations")
            .child(reservation!!.designerId).child(firebaseStringPathInput()).child(auth!!.currentUser!!.uid)

        ReservationDesignerDB.removeValue()
        ReservationUserDB.removeValue()







        val gotoHomeButton = findViewById<TextView>(R.id.go_to_home)
        val gotoReservationListButton = findViewById<TextView>(R.id.go_to_reservation_list)



        gotoHomeButton.setOnClickListener {

        }

        gotoReservationListButton.setOnClickListener {

        }
    }


    /**
     * 수신 인텐트로 전달받은 예약 정보 저장
     */
    private fun getDataFromIntent() {
        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }

        reservation = extras!!.getParcelable("reservation")!!   // 예약 객체 읽기

        editTextReason = extras.getString("editTextReason").toString()//선택한 옵션

        //날짜 필요
        if (reservation == null) { //디자이너 객체가 없다면 종료
            finish()
        }
    }

    /**
     * 파이어베이스 child에 들어갈 경로 (예약 key를 예약 시간 타임스탬프로 설정)
     */

    private fun firebaseStringPathInput() : String{

        return (reservation!!.startTime/1000/86400).toString()
    }


}