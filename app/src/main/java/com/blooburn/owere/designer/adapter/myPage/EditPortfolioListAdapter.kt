package com.blooburn.owere.designer.adapter.myPage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.bumptech.glide.Glide
import com.google.firebase.storage.StorageReference

class EditPortfolioListAdapter(val mContext: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val PORTFOLIO_VIEW_TYPE = 0
    private val ADD_PHOTO_VIEW_TYPE = 1

    private var portfolioPathList = mutableListOf<StorageReference>()

    inner class PortfolioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val portfolioImgView: ImageView =
            itemView.findViewById(R.id.img_view_item_edit_portfolio)
        private val removeBtn: ImageButton =
            itemView.findViewById(R.id.btn_item_edit_portfolio_remove)

        fun bind(position: Int) {
            Glide.with(mContext)
                .load(portfolioPathList[position])
                .into(portfolioImgView)

            removeBtn.setOnClickListener {
                Toast.makeText(mContext, "삭제하시겠습니까? 클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class AddPhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind() {
            itemView.setOnClickListener {
                Toast.makeText(mContext, "Add Photo Clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(position < portfolioPathList.size) {
            true -> PORTFOLIO_VIEW_TYPE
            else -> ADD_PHOTO_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == PORTFOLIO_VIEW_TYPE){
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_edit_designer_portfolio, parent, false)
            PortfolioViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_edit_designer_portfolio_add, parent, false)
            AddPhotoViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == PORTFOLIO_VIEW_TYPE) (holder as PortfolioViewHolder).bind(position)
        else (holder as AddPhotoViewHolder).bind()
    }

    override fun getItemCount(): Int {
        return portfolioPathList.size + 1
    }

    fun setList(inputList: MutableList<StorageReference>){
        portfolioPathList = inputList
    }
}