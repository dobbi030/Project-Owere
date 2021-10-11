package com.blooburn.owere.activity.userMain.userReservation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.blooburn.owere.R
import com.blooburn.owere.fragment.userMainFragment.reservationFragment.ReservationBottomDialogFrament

class ReservationInfo : AppCompatActivity() {



    val reservationBottomDialogFragment : ReservationBottomDialogFrament? = ReservationBottomDialogFrament()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_info)



        initButton()

    }

    private fun initButton(){
        val buttonMenu = findViewById<ImageView>(R.id.ReservationMenuButton)

        buttonMenu.setOnClickListener {
            reservationBottomDialogFragment?.show(supportFragmentManager, reservationBottomDialogFragment.tag)
        }
    }
}