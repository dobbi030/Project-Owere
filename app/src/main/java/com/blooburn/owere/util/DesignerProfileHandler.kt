package com.blooburn.owere.util

import android.view.View
import android.widget.ImageView
import com.blooburn.owere.R
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

interface DesignerProfileHandler {

    /**
     * 평점 -> 별점 변환
     */
    fun convertRatingToStar(rating: Double): String {
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
    fun bindProfileImage(itemView: View, imageView: ImageView, imageLocation: String, circle: Boolean){

        // 임시 예외 처리
        if (imageLocation.isEmpty()){
            Glide.with(itemView.context)
                .load(R.drawable.icon_person_24)
                .circleCrop()
                .into(imageView)

            return
        }

        val imageReference = FirebaseStorage.getInstance().reference.child(imageLocation)   // 프로필 이미지가 있는 스토리지 참조

        when(circle) {
            true -> Glide.with(itemView.context)
                .load(imageReference)
                .circleCrop()
                .into(imageView)

            false -> Glide.with(itemView.context)
                .load(imageReference)
                .centerCrop()
                .into(imageView)
        }

    }

    /**
     * 리뷰 개수 텍스트 반환
     */
    fun buildReviewCountString(count: Int): String {
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
}