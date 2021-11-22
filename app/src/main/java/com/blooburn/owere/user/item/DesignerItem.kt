package com.blooburn.owere.user.item

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DesignerItem(
    var designerId: String,
    val introduction: String,
    val area: String,
    val matchingRate: Int,
    val name: String,
    val profileImagePath: String,
    val rating: Double,
    val reviewCount: Int,
    val number : String,
    val portfolio : String,
    var reviewImages : MutableList<String>,
    var reviews : ArrayList<UserReview>
) : Parcelable {

    constructor()
            : this("","", "", 0, "",
        "", 0.0, 0,"","",
        mutableListOf<String>(), ArrayList<UserReview>()
    )
}
