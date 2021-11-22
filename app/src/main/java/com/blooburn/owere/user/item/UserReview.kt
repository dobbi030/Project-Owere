package com.blooburn.owere.user.item

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserReview(
    val userImagePath: String,
    val userName: String,
    val dates: String,
    val description: String,
    val rating: Double
): Parcelable {
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("","", "", "", 0.0)
}
