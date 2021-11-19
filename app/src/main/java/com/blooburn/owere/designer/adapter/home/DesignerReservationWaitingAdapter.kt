package com.blooburn.owere.designer.adapter.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ItemReservedUserBinding
import com.blooburn.owere.designer.activity.main.DesignerReservationDetailActivity
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.util.DATE_STAMP_KEY
import com.blooburn.owere.util.DesignerProfileHandler
import com.blooburn.owere.util.TypeOfReservation
import com.blooburn.owere.util.UID_KEY
import java.text.SimpleDateFormat
import java.util.*

//수락 대기중인 고객의 예약리스트 어답터
class DesignerReservationWaitingAdapter :
    RecyclerView.Adapter<DesignerReservationWaitingAdapter.ViewHolder>(), DesignerProfileHandler {

    private var reservationList = mutableListOf<DesignerReservation>()
    private var selectedDateStamp = mutableMapOf<Long,Int>()
    private var selectedDate : Long = 0

    inner class ViewHolder(private val binding: ItemReservedUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val reservation = reservationList[position]
            val itemView = this.itemView

            bindProfileImage(
                itemView,
                binding.imageReservedUser,
                reservation.profileImagePath,
                true
            )
            binding.textReservedUserName.text = reservation.userName
            binding.textReservedUserShop.text = reservation.shop
            binding.textReservedUserTime.text = getTreatmentTime(itemView, reservation)

            initUIDependingOnType(binding, reservation.type)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DesignerReservationWaitingAdapter.ViewHolder {
        val view =
            ItemReservedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DesignerReservationWaitingAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(position)

        holder.itemView.setOnClickListener{

            selectedDateStamp.forEach {
                if(position<=it.value){
                    selectedDate = it.key
                    return@forEach
                }

            }

                // userId 전달 -> 수신 액티비티에서 userId로 DB의 예약정보에 접근
                val intent =
                    Intent(holder.itemView.context, DesignerReservationDetailActivity::class.java).apply{
                        putExtra(UID_KEY, reservationList[position].userId)
                        putExtra(DATE_STAMP_KEY, selectedDate)
                    }

                holder.itemView.context.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    fun addData(newDateStamp: Long, list: MutableList<DesignerReservation>) {

        //해당 날짜 스탬프와 해당날짜인 예약 수
        selectedDateStamp.put(newDateStamp,reservationList.size + list.size)     // 프래그먼트에서 지금 보여주고 있는 날짜의
        if(reservationList.isEmpty()){
            reservationList = list
        }else{
            for(i in 0..list.size-1){
                reservationList.add(list[i])
            }
        }


        notifyDataSetChanged()
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
    private fun getTreatmentTime(itemView: View, reservation: DesignerReservation): String{
        return itemView.context.getString(
            R.string.reservation_time,
            convertMilliSecondsToTimeString(reservation.startTime),
            convertMilliSecondsToTimeString(reservation.endTime)
        )
    }

    /**
     * 예약 타입(예정, 시술완료, 정산완료)에 따라 뷰 UI를 다르게 보여준다
     */
    private fun initUIDependingOnType(binding: ItemReservedUserBinding, type: Int){
        val arrowImageView = binding.imageReservedUserArrow
        val settlementTextView = binding.textReservedUserSettle
        val context = binding.root.context

        when (type) {

            TypeOfReservation.ACCEPTED.value -> {

                arrowImageView.visibility = View.GONE
                settlementTextView.visibility = View.GONE
            }

            TypeOfReservation.SCHEDULED.value -> {
                arrowImageView.visibility = View.VISIBLE
                settlementTextView.visibility = View.GONE
            }

            TypeOfReservation.TREATED.value ->{
                settlementTextView.text = context.getString(R.string.settling_fee)
                settlementTextView.setTextColor(context.getColor(R.color.red))
            }

            TypeOfReservation.SETTLED.value ->{
                settlementTextView.text = context.getString(R.string.settlement_completed_blue)
                settlementTextView.setTextColor(context.getColor(R.color.blue_00CCFF))
            }
        }
    }

}