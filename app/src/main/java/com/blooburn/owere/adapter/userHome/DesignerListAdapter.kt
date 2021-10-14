package com.blooburn.owere.adapter.userHome

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ItemUserDesignerListBinding
import com.blooburn.owere.item.UserDesignerItem
import com.bumptech.glide.Glide
import com.firebase.ui.storage.images.FirebaseImageLoader
import com.google.firebase.storage.FirebaseStorage

class DesignerListAdapter() :
    RecyclerView.Adapter<DesignerListAdapter.ViewHolder>() {

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
            binding.textUserDesignerRating.text = convertRatingToStar(designer.rating)
            bindProfileImage(this.itemView, binding.imageUserDesigner, designer.profileImagePath)
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

    /**
     * 리뷰 개수 텍스트 반환
     */
    private fun buildReviewCountString(count: Int): String {
        return StringBuilder()
            .append("리뷰(")
            .append(
                when (count > 50) {
                    true -> "50+)"
                    else -> "$count)"
                }
            )
            .toString()
    }

    /**
     * 평점 -> 별점 변환
     */
    private fun convertRatingToStar(rating: Float): String {
        return when{
            rating < 0 -> ""
            0 <= rating && rating < 1  -> "☆"
            1 <= rating && rating < 1.5 -> "★☆"
            1.5 <= rating && rating < 2 -> "★★"
            2 <= rating && rating < 2.5 -> "★★☆"
            2.5 <= rating && rating < 3 -> "★★★"
            3 <= rating && rating < 3.5 -> "★★★☆"
            3.5 <= rating && rating < 4 -> "★★★★"
            4 <= rating && rating < 4.5 -> "★★★★☆"
            else -> "★★★★★"
        }
    }

    /**
     * 프로필 이미지 적용
     */
    private fun bindProfileImage(itemView: View, imageView: ImageView, imageLocation: String){

        // 임시 예외 처리
        if (imageLocation.isEmpty()){
            Glide.with(itemView.context)
                .load(R.drawable.icon_person_24)
                .circleCrop()
                .into(imageView)

            return
        }

        val imageReference = firebaseStorage.child(imageLocation)
        Log.d("이미지", "$imageReference")

        Glide.with(itemView.context)
            .load(imageReference)
            .circleCrop()
            .into(imageView)
    }
}