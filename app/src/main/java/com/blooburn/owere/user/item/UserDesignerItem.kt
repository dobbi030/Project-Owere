package com.blooburn.owere.user.item

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDesignerItem(
    var designerId: String,
    val area: String,
    val matchingRate: Int,
    val name: String,
    val profileImagePath: String,
    val rating: Double,
    val reviewCount: Int,
) : Parcelable {

    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("", "", 0, "", "", 0.0, 0)
}