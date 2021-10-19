package com.blooburn.owere.adapter.userHome

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.activity.userMain.homeActivity.UserDesignerProfileActivity
import com.blooburn.owere.databinding.ActivityUserDesignerProfileBinding
import com.blooburn.owere.databinding.ItemUserDesignerListBinding
import com.blooburn.owere.item.UserDesignerItem
import com.blooburn.owere.util.DesignerProfileHandler
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage

class DesignerListAdapter :
    RecyclerView.Adapter<DesignerListAdapter.ViewHolder>(), DesignerProfileHandler {

    private val designerList = mutableListOf<UserDesignerItem>()
    private val firebaseStorage = FirebaseStorage.getInstance().reference

    inner class ViewHolder(private val binding: ItemUserDesignerListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            val designer = designerList[position]

            binding.textUserDesignerName.text = designer.name
            binding.textUserDesignerArea.text = designer.area
            binding.textUserDesignerReviewCount.text = buildReviewCountString(designer.reviewCount)
            binding.textUserDesignerMatching.text =
                this.itemView.context.getString(R.string.matching_rate, designer.matchingRate)
            binding.textUserDesignerStar.text = convertRatingToStar(designer.rating)
            bindProfileImage(this.itemView, binding.imageUserDesigner, designer.profileImagePath, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserDesignerListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        // 디자이너 프로필 화면을 띄운다
        holder.itemView.setOnClickListener{
            // 디자이너 객체 전달
            val intent = Intent(holder.itemView.context, UserDesignerProfileActivity::class.java).apply{
                putExtra("designerData", designerList[position])
            }

            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return designerList.size
    }

    // 디자이너 리스트 전체 삭제
    fun clearList() = designerList.clear()

    // 다자이너 아이템 추가
    fun addData(designerInfo: UserDesignerItem){
        designerList.add(designerInfo)
    }
}