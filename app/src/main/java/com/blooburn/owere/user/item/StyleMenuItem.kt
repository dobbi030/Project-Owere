package com.blooburn.owere.user.item

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StyleMenuItem(
    override val menuName: String, //앞머리
    override val menuPrice: String, //5000원
    override val menuTime: String,  //10분
    //길이추가
    val addLength: Int, //디자이너가 길이 추가 옵션을 추가했는지 여부 0, 1
    // 커트포함
    val withCut: Int    //디자이너가 커트포함 옵션을 추가했는지 여부 0, 1
): MenuItem(), Parcelable {
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("", "", "",0, 0)
}