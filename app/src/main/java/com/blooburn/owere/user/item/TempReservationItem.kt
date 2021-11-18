package com.blooburn.owere.user.item

import com.blooburn.owere.util.TypeOfReservation

data class TempReservationItem(
    val reservationNumber:String,
    val seatNumber: Int,
    val Start: Long,
    val end: Long,
    val userName: String,
    val designerName: String,
    val date: Long,
    val menuItem: StyleMenuItem?, //선택 메뉴
    val option: String ,//옵션 추가(길이, 커트)
    var type: TypeOfReservation

){
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("", 0, 0,0,"","",0,null,"", TypeOfReservation.SCHEDULED)
}
