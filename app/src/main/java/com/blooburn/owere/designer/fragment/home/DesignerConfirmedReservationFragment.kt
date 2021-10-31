package com.blooburn.owere.designer.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blooburn.owere.R
import com.blooburn.owere.databinding.DesignerConfirmedReservationFragmentBinding
import com.blooburn.owere.designer.adapter.home.DesignerReservationListAdapter
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.util.CustomDividerDecoration
import com.blooburn.owere.util.TypeOfDesignerReservation
import com.blooburn.owere.util.ZONE_ID
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.temporal.ChronoField
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DesignerConfirmedReservationFragment :
    Fragment(R.layout.designer_confirmed_reservation_fragment) {

    private val tempDesignerId = "designer0"
    private var tempEpochDay = 0L
    private val reservationsReferencePath = "designerReservations/$tempDesignerId/"
    private var binding: DesignerConfirmedReservationFragmentBinding? = null

    private lateinit var scheduledAdapter: DesignerReservationListAdapter
    private lateinit var completedAdapter: DesignerReservationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DesignerConfirmedReservationFragmentBinding.bind(view)

        // 예정된 예약 리사이클러뷰
        binding?.recyclerDesignerConfirmedReservationScheduled?.apply {

            // custom divider 적용
            addItemDecoration(
                CustomDividerDecoration(1f, ContextCompat.getColor(this.context, R.color.gray_cc))
            )

            layoutManager = LinearLayoutManager(context)
            scheduledAdapter = DesignerReservationListAdapter()
            adapter = scheduledAdapter
        }

        // 완료된 예약 리사이클러뷰
        binding?.recyclerDesignerConfirmedReservationCompleted?.apply {

            // custom divider 적용
            addItemDecoration(
                CustomDividerDecoration(1f, ContextCompat.getColor(this.context, R.color.gray_cc))
            )

            layoutManager = LinearLayoutManager(context)
            completedAdapter = DesignerReservationListAdapter()
            adapter = completedAdapter
        }

        binding?.calendarDesignerConfirmedReservation?.currentDate = CalendarDay.today()
        binding?.calendarDesignerConfirmedReservation?.setOnDateChangedListener { calendarView, date, isSelected ->
            if (isSelected) {
                tempEpochDay = date.date.getLong(ChronoField.EPOCH_DAY)
                val referencePathOfSelectedDay = reservationsReferencePath + tempEpochDay

                databaseInstance.reference.child(referencePathOfSelectedDay)
                    .addListenerForSingleValueEvent(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {

                            val scheduledList = mutableListOf<DesignerReservation>()
                            val completedList = mutableListOf<DesignerReservation>()
                            val currentTime = LocalTime.now().toSecondOfDay() * 1000

                            snapshot.children.forEach { reservationSnapshot ->
                                val reservation =
                                    reservationSnapshot.getValue(DesignerReservation::class.java)

                                Log.d("시간", "currentTime: $currentTime, endTime: ${reservation?.endTime!!}")
                                // 끝나는 시간이 현재 시간을 지났을 때
                                if (currentTime < reservation?.endTime!!) {
                                    reservation.type = TypeOfDesignerReservation.COMPLETED
                                    completedList.add(reservation)
                                }else{
                                    scheduledList.add(reservation)
                                }
                            }

                            scheduledAdapter.setData(scheduledList)
                            completedAdapter.setData(completedList)
                        }

                        override fun onCancelled(error: DatabaseError) {}
                    })
            }
            val date0 = LocalDate.of(2021, 10, 29)
            val date1 = LocalDate.of(2021, 11, 1)
            val date2 = LocalDate.of(2021, 11, 2)
            Log.d(
                "날짜",
                "${date0.getLong(ChronoField.EPOCH_DAY)}, ${date1.getLong(ChronoField.EPOCH_DAY)}, ${
                    date2.getLong(ChronoField.EPOCH_DAY)
                }"
            )
            val time0_1 = LocalTime.of(10, 30).toSecondOfDay() * 1000
            val time0_2 = LocalTime.of(11, 0).toSecondOfDay() * 1000
            val time1_1 = LocalTime.of(15, 30).toSecondOfDay() * 1000
            val time1_2 = LocalTime.of(16, 0).toSecondOfDay() * 1000
            val time2_1 = LocalTime.of(16, 30).toSecondOfDay() * 1000
            val time2_2 = LocalTime.of(19, 0).toSecondOfDay() * 1000
            Log.d("시간", "$time0_1, $time0_2,\n$time1_1, $time1_2,\n$time2_1, $time2_2")
        }
    }

    private fun getReservations() {

    }
}