package com.blooburn.owere.user.adapter.userReservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.databinding.ItemUserSalonListBinding
import com.blooburn.owere.user.item.ShopListItem
import com.blooburn.owere.util.DesignerProfileHandler


/**
 * 디자이너가 설정한 미용실 리스트를 위한 어답터
 */
class ShopListOfDesignerAdapter(val onItemClicked: (ShopListItem) -> Unit?) : RecyclerView.Adapter<ShopListOfDesignerAdapter.ViewHolder>(),DesignerProfileHandler{


    private var shopList = mutableListOf<ShopListItem>()
    inner class ViewHolder(private val binding: ItemUserSalonListBinding) :RecyclerView.ViewHolder(
        binding.root
    ){

        fun bind(position: Int) {


            val shoplist = shopList[position]
            //컴포넌트 클릭 이벤트 //액티비티에서 구현
            binding.root.setOnClickListener {
                onItemClicked(shoplist)
            }
            //이름
            binding.reserveSalonNameText.text = shoplist.name
            //리뷰 카운트
            binding.reserveSalonDesignerReview.text = buildReviewCountString(shoplist.reviewCount)
            //별점(핸들러 인터페이스 사용)
            binding.reserveSalonRatingText.text = convertRatingToStar(shoplist.rating)
            //주소
            binding.reserveSalonAddressText.text= shoplist.area

        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = ItemUserSalonListBinding.inflate(
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


      return shopList.size
    }

    /**
     * 리사이클러 뷰에 추가된 리스트를 추가해줌
     */
    fun addData(shopListItem: ShopListItem){
        shopList.add(shopListItem)
    }

    /** 디자이너 리스트 전체 삭제
     *
     */
    fun clearList() = shopList.clear()

}