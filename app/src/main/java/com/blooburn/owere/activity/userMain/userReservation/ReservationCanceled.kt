package com.blooburn.owere.activity.userMain.userReservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.blooburn.owere.R

class ReservationCanceled : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_canceled)


        val gotoHomeButton = findViewById<TextView>(R.id.go_to_home)
        val gotoReservationListButton = findViewById<TextView>(R.id.go_to_reservation_list)

        gotoHomeButton.setOnClickListener {

        }

        gotoReservationListButton.setOnClickListener {

        }
    }
}