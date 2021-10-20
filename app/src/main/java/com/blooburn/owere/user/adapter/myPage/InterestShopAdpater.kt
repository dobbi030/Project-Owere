package com.blooburn.owere.user.adapter.myPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.blooburn.owere.databinding.MypageFavoriteShopBindingBinding
import com.blooburn.owere.user.item.FavoriteShop

class InterestShopAdpater:
    ListAdapter<FavoriteShop, InterestShopAdpater.ViewHolder>(diffUtil) {


    inner class ViewHolder(private val binding: MypageFavoriteShopBindingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteShop: FavoriteShop) {

//            컴포넌트 클릭 이벤트
//            binding.root.setOnClickListener {
//                onItemClicked(chatListItem)
////            }
//                //채팅방 상대방을 타이틀로 설정
//                //채팅방 유저 명단을 받아옴
//                var chatTitle = chatListItem.opponentName
//                //마지막 메시지
//                var chatLastMessage = chatListItem.lastMessage

            var myShopName = favoriteShop.name
            var myshopArea = favoriteShop.area
            var myShopRating = favoriteShop.rating
            var myShopReviewCount = favoriteShop.reviewCount
//                //디자이너 이름 표시
            binding.shopName.text = myShopName
            binding.shopAddressText.text = myshopArea
            binding.reviewCountText.text = myShopReviewCount.toString()
            binding.ratingText.text = myShopRating.toString()

            //하트리스너 누르면 좋아요 취소
            binding.shopLikeImage.setOnClickListener {
                binding.shopLikeImage.visibility = View.GONE
                binding.shopUnlikeImage.visibility = View.VISIBLE
            }
            binding.shopUnlikeImage.setOnClickListener {
                binding.shopUnlikeImage.visibility = View.GONE
                binding.shopLikeImage.visibility = View.VISIBLE
            }
//

//                binding.chattinglistLastmessage.text = chatLastMessage
////            프로필 표시 필요
//                binding.chattinglistProfile
////            시간 표시 필요
//
//
        }
//
//
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            MypageFavoriteShopBindingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(currentList[position])
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<FavoriteShop>() {
            override fun areItemsTheSame(
                oldItem: FavoriteShop,
                newItem: FavoriteShop
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FavoriteShop,
                newItem: FavoriteShop
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}

