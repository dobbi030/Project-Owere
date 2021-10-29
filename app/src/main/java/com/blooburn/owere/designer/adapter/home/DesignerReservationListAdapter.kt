package com.blooburn.owere.designer.adapter.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ItemReservedUserBinding
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.util.TypeOfDesignerReservation
import com.blooburn.owere.util.DesignerProfileHandler
import java.text.SimpleDateFormat
import java.util.*

class DesignerReservationListAdapter :
    RecyclerView.Adapter<DesignerReservationListAdapter.ViewHolder>(), DesignerProfileHandler {

    private var reservationList = mutableListOf<DesignerReservation>()

    open inner class ViewHolder(private val binding: ItemReservedUserBinding) :
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
            binding.textReservedUserTime.text =
                itemView.context.getString(
                    R.string.reservation_time,
                    convertMilliSecondsToTimeString(reservation.startTime),
                    convertMilliSecondsToTimeString(reservation.endTime)
                )

            when (reservation.type) {
                TypeOfDesignerReservation.SCHEDULED -> {
                    binding.imageReservedUserArrow.visibility = View.VISIBLE
                    binding.textReservedUserSettle.visibility = View.GONE
                }
                TypeOfDesignerReservation.COMPLETED -> {
                    binding.textReservedUserSettle.text =
                        this.itemView.context.getString(R.string.settling_fee)
                }
                TypeOfDesignerReservation.SETTLED -> {
                    binding.textReservedUserSettle.text =
                        this.itemView.context.getString(R.string.settlement_completed)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DesignerReservationListAdapter.ViewHolder {
        val view =
            ItemReservedUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DesignerReservationListAdapter.ViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return reservationList.size
    }

    fun setData(list: MutableList<DesignerReservation>) {
        reservationList = list
        notifyDataSetChanged()
    }

    private fun convertMilliSecondsToTimeString(milliSeconds: Long): String {
        val formatter = SimpleDateFormat("a kk:mm", Locale.KOREA).apply {
            timeZone = TimeZone.getTimeZone("KST")
        }

        return formatter.format(milliSeconds)
    }

}