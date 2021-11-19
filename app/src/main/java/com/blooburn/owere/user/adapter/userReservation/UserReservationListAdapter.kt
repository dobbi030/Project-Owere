package com.blooburn.owere.user.adapter.userReservation

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ItemReservedUserBinding
import com.blooburn.owere.user.activity.main.userReservation.UserReservationDetailActivity
import com.blooburn.owere.user.item.ReservationListItem
import com.blooburn.owere.util.DesignerProfileHandler
import com.blooburn.owere.util.TypeOfDesignerReservation
import java.text.SimpleDateFormat
import java.util.*

//유저에게 예약내역을 보여주기 위한 리사이클러뷰 어답터
class UserReservationListAdapter:
RecyclerView.Adapter<UserReservationListAdapter.ViewHolder>(), DesignerProfileHandler {

    private var reservationList = mutableListOf<ReservationListItem>()

    inner class ViewHolder(private val binding: ItemReservedUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val reservation = reservationList[position]
            val itemView = this.itemView

            bindProfileImage(
                itemView,
                binding.imageReservedUser,
                reservation.profile,
                true
            )

            binding.textReservedUserName.text = reservation.designerName
            binding.textReservedUserShop.text = reservation.shop
            binding.textReservedUserTime.text = getTreatmentTime(itemView, reservation)


        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserReservationListAdapter.ViewHolder {
        val view =
            ItemReservedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: UserReservationListAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(position)

        holder.itemView.setOnClickListener{
            val intent =
                Intent(holder.itemView.context, UserReservationDetailActivity::class.java).apply{
                    putExtra("userReservationDetail", reservationList[position])
                }

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    fun setData(list: MutableList<ReservationListItem>) {
        reservationList = list
        notifyDataSetChanged()
    }

    private fun convertMilliSecondsToTimeString(milliSeconds: Long): String {
        val formatter = SimpleDateFormat("kk:mm", Locale.KOREA).apply {
            timeZone = TimeZone.getTimeZone("KST")
        }

        return formatter.format(milliSeconds)
    }

    //유저 Reservation
    //designerName: "디자이너1"
    //endTime: 39600000
    //profileImagePath: "profileImages/users/37mCYRBcd2QRRg6f9LjI3SqIclY..."
    //shop: "오월 미용실"
    //startTime: 37800000
    //userName: "박성준"

    private fun getTreatmentTime(itemView: View, reservation: ReservationListItem): String{
        return itemView.context.getString(
            R.string.reservation_time,
            convertMilliSecondsToTimeString(reservation.startTime*1000),
            convertMilliSecondsToTimeString(reservation.endTime*1000)
        )
    }

    private fun initDependingOnType(binding: ItemReservedUserBinding, type: TypeOfDesignerReservation){
        val arrowImageView = binding.imageReservedUserArrow
        val settlementTextView = binding.textReservedUserSettle
        val context = binding.root.context

        when (type) {
            TypeOfDesignerReservation.SCHEDULED -> {
                arrowImageView.visibility = View.VISIBLE
                settlementTextView.visibility = View.GONE
            }

            TypeOfDesignerReservation.COMPLETED ->
                settlementTextView.text = context.getString(R.string.settling_fee)

            TypeOfDesignerReservation.SETTLED ->
                settlementTextView.text = context.getString(R.string.settlement_completed_blue)

        }
    }

}