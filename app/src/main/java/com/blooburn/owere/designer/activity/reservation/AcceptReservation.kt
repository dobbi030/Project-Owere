package com.blooburn.owere.designer.activity.reservation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.main.UserMainActivity

//디자이너 예약내역(수락대기중) -> 예약 수락 화면
class AcceptReservation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept_reservation)


        initButton()


    }

    /**
     * 디자이너 홈으로 가기 버튼
     */
    private fun initButton(){
        var button = findViewById<TextView>(R.id.accept_reservation_go_to_home_button)


        button.setOnClickListener {
            val intent = Intent(this, UserMainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            //FLAG_ACTIVITY_CLEAR_TOP 사용하여 홈 액티비티가 호출 될 시 그 위에 있던 액티비티 모두 삭제
            startActivity(intent)
        }

    }
}