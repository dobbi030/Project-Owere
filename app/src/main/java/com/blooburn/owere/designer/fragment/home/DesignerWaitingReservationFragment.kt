package com.blooburn.owere.designer.fragment.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.WaitingFragmentLayoutBinding
import com.blooburn.owere.designer.adapter.home.DesignerReservationListAdapter
import com.blooburn.owere.designer.adapter.home.DesignerReservationWaitingAdapter
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.user.adapter.userReservation.UserReservationListAdapter
import com.blooburn.owere.user.item.ReservationListItem
import com.blooburn.owere.util.CustomDividerDecoration
import com.blooburn.owere.util.TypeOfReservation
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

//예정된 예약
//대기중 예약
class DesignerWaitingReservationFragment : Fragment(R.layout.waiting_fragment_layout) {

    private var binding: WaitingFragmentLayoutBinding? = null
    private val tempDesignerId = "designer0"

    private lateinit var auth: FirebaseAuth
    private lateinit var reservationsReferencePath: String


    private lateinit var waitingAdapter: DesignerReservationWaitingAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        //DB 레퍼런스 -> 디자이너의 예약 리스트
        reservationsReferencePath = "designerReservations/${tempDesignerId}/"


        //바인딩 초기화
        val waitingFragmentLayoutBinding = WaitingFragmentLayoutBinding.bind(view)
        binding = waitingFragmentLayoutBinding


        //확정된 예약 리사이클러뷰 초기화
        waitingAdapter = DesignerReservationWaitingAdapter()
        binding?.recyclerviewWaiting?.adapter = waitingAdapter
        binding?.recyclerviewWaiting?.layoutManager = LinearLayoutManager(requireContext())


//        initReservationsRecyclerView(
//            binding?.recyclerviewConfirmed!!,
//            confirmededAdapter
//        )

        databaseInstance.reference.child(reservationsReferencePath)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var epochDay = 0L

                    var waitingReservation = mutableListOf<DesignerReservation>()

                    snapshot.children.forEach { DateSnapshot ->

                        DateSnapshot.children.forEach { reservationSnapshot ->
                            var reservation: DesignerReservation? =
                                reservationSnapshot.getValue(DesignerReservation::class.java)

                            val currentTime = System.currentTimeMillis()
                            val reservationStart = reservation!!.startTime
                            val reservationEnd = reservation!!.endTime

                            //날짜
                            epochDay = reservationStart / 86400

                            // 끝나는 시간이 현재 시간을 지나지 않았을 때 && 디자이너의 수락 기다림
                            //currentTime > reservationEnd &&
                            if (reservation.type == TypeOfReservation.ACCEPTED.value) {
                                waitingReservation.add(reservation)

                                //해당 날짜의 예약 내역 넘겨주기

                            }



                        }
                        waitingAdapter.addData(epochDay, waitingReservation)
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }
            })


    }

    /**
     * 예약 리사이클러뷰 초기화
     */
    private fun initReservationsRecyclerView(
        recyclerView: RecyclerView,
        _adapter: UserReservationListAdapter
    ) {
        recyclerView.apply {
            // custom divider 적용
            addItemDecoration(
                CustomDividerDecoration(1f, ContextCompat.getColor(this.context, R.color.gray_cc))
            )

            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }
    }
}
