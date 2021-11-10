package com.blooburn.owere.user.adapter.userReservation


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R

import com.blooburn.owere.databinding.ItemReserveTimeTabBinding

import com.blooburn.owere.user.activity.main.userReservation.SelectTimeInterface


//시간 날짜 예약 레이아웃에서 시간 선택 버튼 리사이클러뷰 어답터(격자형)
class ReserveTimeAdapter(private val selectTimeInterface: SelectTimeInterface) : RecyclerView.Adapter<ReserveTimeAdapter.ViewHolder>() {



    var selectedTime :CheckBox? = null
    var selectedIndex = 0
    //뷰홀더
    private val timeList = mutableListOf<String>()

    inner class ViewHolder(var binding : ItemReserveTimeTabBinding):
            RecyclerView.ViewHolder(
                binding.root
            ){
                fun bind(position : Int){
                    binding.timeTab.text = timeList[position]
                    selectTimeInterface.addTime(binding.timeTab)


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

        //각각의 시간 선택 버튼(체크박스로 구현)
        var checkButton  = holder.itemView.findViewById<CheckBox>(R.id.timeTab)
        //인터페이스의 함수를 호출하여 체크박스를 리스트에 추가
        selectTimeInterface.addTime(checkButton)// 생성되는 버튼들 인터페이스에 정의된 배열에 추가
        //시간버튼을 누를 때 리스너
        checkButton.setOnClickListener {
            selectTimeInterface.reserveTime = checkButton
            if (checkButton.isChecked) {
                for (i in 0..selectTimeInterface.checklist.size - 1) {
                    //추가된 시간 버튼 리스트 중 선택한 시간버튼과 같은 것을 찾음
                    if (selectTimeInterface.checklist.get(i) == selectTimeInterface.reserveTime) {
                        selectedIndex = i
                        //선택한 시간을 액티비티에 넘겨주기위해 할당
                        selectedTime = selectTimeInterface.reserveTime
                    } else {
                        selectTimeInterface.checklist.get(i).isChecked = false
                    }

                }
            } else {
                selectedIndex = -1
            }

        }

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