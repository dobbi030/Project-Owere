package com.blooburn.owere.user.item

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//미용실 객체
@Parcelize
data class ShopListItem(

    val area: String,//주소
    val name: String,
    val profileImagePath: String,
    val rating: Double,
    val reviewCount: Int,
    var latitude: Double,   //위도
    var longitude: Double  //경도

) : Parcelable {
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("", "", "", 0.0, 0,0.0,0.0)
}

