package com.blooburn.owere.designer.activity.reservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.blooburn.owere.R
import com.blooburn.owere.designer.item.DesignerReservationDetail
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.blooburn.owere.util.databaseInstance

//디자이너의 예약 거절 결과 액티비티
class RefuseReservation : AppCompatActivity() {

    private var timeStamp : Long = 0
    private var dateStamp : Long = 0
    private var reservation : DesignerReservationDetail? = null
    private var designerId =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_refuse_reservation)

        getDataFromIntent()
        deleteReservation()
    }

    /**
     * 수신 인텐트로 전달받은 예약 정보 저장
     */
    private fun getDataFromIntent() {
        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }

        reservation = extras!!.getParcelable("reservation")!!   // DesignerData 객체 읽기

        dateStamp = extras!!.getLong("dateStamp")
        timeStamp = dateStamp*86400000 + reservation!!.startTime

        designerId = extras!!.getString("designerId")!!
        if (reservation == null) { //예약 객체가 없다면 종료
            Toast.makeText(this,"예약 정보가 없습니다.",Toast.LENGTH_LONG)
            finish()
        }
    }
    private fun deleteReservation(){
        //유저 DB에서 삭제
        databaseInstance.reference.child("UserReservation").child("${reservation!!.userId}")
            .child("${timeStamp}").removeValue()
        //디자이너 DB에서 삭제
        databaseInstance.reference.child("designerReservations").child("${designerId}")
            .child("${dateStamp}").child(reservation!!.userId).removeValue()
    }
}