package com.blooburn.owere.user.item

import android.os.Parcelable
import com.blooburn.owere.util.TypeOfDesignerReservation
import kotlinx.android.parcel.Parcelize

//예약 리스트에 보여줄 객체
@Parcelize
data class ReservationListItem(

    val profile : String, //디자이너 프로필
    val designerName : String, //디자이너 이름
    val shop : String, // 미용실
    val startTime : Long,   //예약 시간
    val endTime : Long,
    val userId : String, //유저 아이디
    val menuItem : StyleMenuItem, //선택 메뉴
    val option : String, //옵션 추가(길이, 커트)
    val accepted : Int //수락여부

) : Parcelable {
    constructor() : this("", "","",0, 0, "", StyleMenuItem(),"",1)
}
//유저 Reservation
//designerName: "디자이너1"
//endTime: 39600000
//profileImagePath: "profileImages/users/37mCYRBcd2QRRg6f9LjI3SqIclY..."
//shop: "오월 미용실"
//startTime: 37800000
//userName: "박성준"