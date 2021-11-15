package com.blooburn.owere.designer.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.DesignerConfirmedReservationFragmentBinding
import com.blooburn.owere.designer.adapter.home.DesignerReservationListAdapter
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.util.CustomDividerDecoration
import com.blooburn.owere.util.TypeOfDesignerReservation
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.temporal.ChronoField
import java.text.SimpleDateFormat
import java.util.*

class DesignerConfirmedReservationFragment :
    Fragment(R.layout.designer_confirmed_reservation_fragment) {


    private val tempDesignerId = "designer0"
    private val reservationsReferencePath = "designerReservations/$tempDesignerId/"
    private val currentDayStamp = LocalDate.now().getLong(ChronoField.EPOCH_DAY)
    private var binding: DesignerConfirmedReservationFragmentBinding? = null

    private lateinit var scheduledAdapter: DesignerReservationListAdapter
    private var scheduledList = mutableListOf<DesignerReservation>()
    private var completedList = mutableListOf<DesignerReservation>()
    private lateinit var completedAdapter: DesignerReservationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DesignerConfirmedReservationFragmentBinding.bind(view)

        // 예정된 예약 리사이클러뷰 초기화
        scheduledAdapter = DesignerReservationListAdapter()
        initReservationsRecyclerView(
            binding?.recyclerDesignerConfirmedReservationScheduled!!,
            scheduledAdapter
        )

        // 완료된 예약 리사이클러뷰 초기화
        completedAdapter = DesignerReservationListAdapter()
        initReservationsRecyclerView(
            binding?.recyclerDesignerConfirmedReservationCompleted!!,
            completedAdapter
        )

        // 달력
        binding?.calendarDesignerConfirmedReservation?.currentDate = CalendarDay.today()
        binding?.calendarDesignerConfirmedReservation?.setOnDateChangedListener(dateSelectedListener)
    }

    /**
     * 예약 리사이클러뷰 초기화
     */
    private fun initReservationsRecyclerView(
        recyclerView: RecyclerView,
        _adapter: DesignerReservationListAdapter
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

    /**
     * 달력 클릭 리스너
     */
    private val dateSelectedListener =
        OnDateSelectedListener { widget, date, isSelected ->
            if (isSelected) {
                updateTodayText(date.date)  // 오늘 날짜 UI 업데이트

                val selectedDayStamp = date.date.getLong(ChronoField.EPOCH_DAY) // 선택된 날짜 스탬프
                val referencePathOfSelectedDay =
                    reservationsReferencePath + selectedDayStamp   // 해당 날짜의 DB 주소

                // 선택 날짜의 예약 목록들을 DB에서 불러와서 업데이트한다
                loadAndSetReservationsFromDB(referencePathOfSelectedDay, selectedDayStamp)
                // 예약 개수 나타내는 UI 업데이트
                updateCountOfReservations(scheduledList.size, completedList.size)
            }
        }

    /**
     * 선택 날짜의 예약 목록들을 DB에서 불러온다
     */
    private fun loadAndSetReservationsFromDB(path: String, selectedDayStamp: Long) {
        databaseInstance.reference.child(path)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {

                    scheduledList = mutableListOf()
                    completedList = mutableListOf()
                    val currentTime = LocalTime.now().toSecondOfDay() * 1000

                    snapshot.children.forEach { reservationSnapshot ->
                        val reservation =
                            reservationSnapshot.getValue(DesignerReservation::class.java)

                        // 예정된, 정산할, 정산된 예약 분류
                        if (reservation != null) {
                            sortReservation(reservation, selectedDayStamp, currentTime)
                        }
                    }

                    scheduledAdapter.setData(scheduledList)
                    completedAdapter.setData(completedList)
                    // 예약 개수 나타내는 UI 업데이트
                    updateCountOfReservations(scheduledList.size, completedList.size)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    /**
     * 예약 불러올 때, 개수 나타내는 text 업데이트
     */
    private fun updateCountOfReservations(scheduledCount: Int, completedCount: Int) {
        binding?.textDesignerConfirmedReservationScheduledCount?.text = if (scheduledCount > 0)
            getString(R.string.total_reservation_count, scheduledCount) else ""
        binding?.textDesignerConfirmedReservationCompletedCount?.text = if (completedCount > 0)
            getString(R.string.total_reservation_count, completedCount) else ""
    }

    /**
     * 오늘 월일 텍스트 업데이트
     */
    private fun updateTodayText(date: LocalDate) {
        binding?.textDesignerConfirmedReservationToday?.text =
            getString(R.string.text_today, date.monthValue, date.dayOfMonth)
    }

    private fun sortReservation(
        reservation: DesignerReservation,
        selectedDayStamp: Long,
        currentTime: Int
    ) {
        // 선택된 날이 과거일 때
        if (selectedDayStamp < currentDayStamp) {
            reservation.type = TypeOfDesignerReservation.COMPLETED
            completedList.add(reservation)
        }
        // 미래일 때
        else if (currentDayStamp < selectedDayStamp) {
            scheduledList.add(reservation)
        } else {
            // 시술 끝나는 시간이 현재 시간을 지났을 때
            if (currentTime < reservation.endTime) {
                reservation.type = TypeOfDesignerReservation.COMPLETED
                completedList.add(reservation)
            } else {
                scheduledList.add(reservation)
            }
        }
    }
}