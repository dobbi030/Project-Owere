package com.blooburn.owere.user.adapter.userReservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.blooburn.owere.databinding.ItemReserveTimeTabBinding
import com.blooburn.owere.user.item.StyleMenuItem

//시간 날짜 예약 레이아웃에서 시간 선택 버튼 리사이클러뷰 어답터(격자형)
class ReserveTimeAdapter : RecyclerView.Adapter<ReserveTimeAdapter.ViewHolder>() {



    //뷰홀더
    private val timeList = mutableListOf<String>()

    inner class ViewHolder(var binding : ItemReserveTimeTabBinding):
            RecyclerView.ViewHolder(
                binding.root
            ){
                fun bind(position : Int){
                    binding.timeTab.text = timeList[position]

                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = ItemReserveTimeTabBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }

    override fun getItemCount(): Int {
        return timeList.size

    }


    //리사이클러 뷰에 추가된 리스트를 추가해줌
    fun addData(time: String) {
        timeList.add(time)
    }

    // 시간 리스트 전체 삭제
    fun clearData(){
        timeList.clear()
    }

}