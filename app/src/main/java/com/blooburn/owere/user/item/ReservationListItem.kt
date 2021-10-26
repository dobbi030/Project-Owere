package com.blooburn.owere.user.item

//예약 리스트에 보여줄 객체
data class ReservationListItem(

    val profile : String, //디자이너 프로필
    val designerName : String, //디자이너 이름
    val shop : String, // 미용실
    val time : String,   //예약 시간
    val userId : String, //유저 아이디
    val menuItem : StyleMenuItem, //선택 메뉴
    val option : String //옵션 추가(길이, 커트)

)
