package com.blooburn.owere.user.adapter.userReservation


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.main.userReservation.CheckboxAddedListener
import com.blooburn.owere.user.activity.main.userReservation.checkBoxlist
import com.blooburn.owere.databinding.ItemReservePriceMenuBinding
import com.blooburn.owere.user.activity.main.userReservation.selectedMenu
import com.blooburn.owere.user.item.StyleMenuItem


class MenuSelectAdapter(private val checkboxAddedListener: CheckboxAddedListener) :
    RecyclerView.Adapter<MenuSelectAdapter.ViewHolder>() {

    var selectedIndex = 0



    //뷰홀더
    private val menuList = mutableListOf<StyleMenuItem>()

    inner class ViewHolder(private val binding: ItemReservePriceMenuBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {

        fun bind(position: Int) {


            val menulist = menuList[position]
            //메뉴 이름
            binding.reservePriceMenuTitle.text = menulist.menuName

            if (menulist.addLength == 1) {
                //옵션 추가
                binding.reserveItemPriceOption.text = "길이추가"

            } else {
                binding.reserveItemPriceOption.text = ""
            }

            //메뉴 가격
            binding.reservePriceMenuPrice.text = "${menulist.menuPrice} 원"

            //메뉴 시간
            binding.reserveItemPriceTime.text = menulist.menuTime

            binding.reserveMenuCheckbox.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    selectedMenu = menulist
                }
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val view = ItemReservePriceMenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(position)
        //각 뷰의 체크박스
        var checkbox = holder.itemView.findViewById<CheckBox>(R.id.reserve_menu_checkbox)
        //인터페이스의 함수를 호출하여 체크박스를 리스트에 추가
        checkboxAddedListener.addCheckBox(checkbox)
        checkbox.setOnClickListener {
            if (checkbox.isChecked) {
                for (i in 0..checkBoxlist.size - 1) {
                    if (checkBoxlist.get(i) == checkbox) {
                        selectedIndex = i
                    } else {
                        checkBoxlist.get(i).isChecked = false
                    }

                }
            } else {
                selectedIndex = -1
            }
        }

    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    //리사이클러 뷰에 추가된 리스트를 추가해줌
    fun addData(stylemenu: StyleMenuItem) {
        menuList.add(stylemenu)
    }

    // 디자이너 리스트 전체 삭제
    fun clearList() = menuList.clear()


}
