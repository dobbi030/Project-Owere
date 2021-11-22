package com.blooburn.owere.user.adapter.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ItemUserDesignerListBinding
import com.blooburn.owere.user.activity.main.homeActivity.UserDesignerProfileActivity
import com.blooburn.owere.user.item.DesignerItem
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.blooburn.owere.util.DesignerProfileHandler

class DesignerListAdapter :
    RecyclerView.Adapter<DesignerListAdapter.ViewHolder>(), DesignerProfileHandler {

    private val designerList = mutableListOf<DesignerItem>()

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
            bindProfileImage(
                this.itemView,
                binding.imageUserDesigner,
                designer.profileImagePath,
                true
            )
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
        holder.itemView.setOnClickListener {
            // 디자이너 객체 전달
            val intent =
                Intent(holder.itemView.context, UserDesignerProfileActivity::class.java).apply {
                    putExtra(DESIGNER_DATA_KEY, designerList[position])
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
    fun addData(designerInfo: DesignerItem) {
        designerList.add(designerInfo)
    }
}