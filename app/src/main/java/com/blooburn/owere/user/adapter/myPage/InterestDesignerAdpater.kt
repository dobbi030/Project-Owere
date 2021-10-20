package com.blooburn.owere.user.adapter.myPage

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

import androidx.recyclerview.widget.RecyclerView

import com.blooburn.owere.databinding.ItemFavoriteDisignerBinding

import com.blooburn.owere.user.item.FavoriteDesigner

class InterestDesignerAdpater :
    ListAdapter<FavoriteDesigner, InterestDesignerAdpater.ViewHolder>(diffUtil) {


    inner class ViewHolder(private val binding: ItemFavoriteDisignerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteDesigner: FavoriteDesigner) {

//            컴포넌트 클릭 이벤트
//            binding.root.setOnClickListener {
//                onItemClicked(chatListItem)
////            }
//                //채팅방 상대방을 타이틀로 설정
//                //채팅방 유저 명단을 받아옴
//                var chatTitle = chatListItem.opponentName
//                //마지막 메시지
//                var chatLastMessage = chatListItem.lastMessage

            var designerName = favoriteDesigner.name
//                //디자이너 이름 표시
            binding.favoriteDesignerName.text = designerName

            //하트리스너 누르면 좋아요 취소
            binding.designerLiked.setOnClickListener {
                binding.designerLiked.visibility = GONE
                binding.designerUnliked.visibility = VISIBLE
            }
            binding.designerUnliked.setOnClickListener {
                binding.designerUnliked.visibility = GONE
                binding.designerLiked.visibility = VISIBLE
            }
//
//
        }
//
//
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemFavoriteDisignerBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<FavoriteDesigner>() {
            override fun areItemsTheSame(
                oldItem: FavoriteDesigner,
                newItem: FavoriteDesigner
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FavoriteDesigner,
                newItem: FavoriteDesigner
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}

