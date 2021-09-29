package com.blooburn.owere.item

//유저별 채팅방 리스트
data class ChatListItem(
    val userId : String,
    val chatRoomId: String, //채팅방 ID
    val lastMessage: String,    //마지막 메시지
    val profileImg: String, //프로필이미지
    val timestamp: Long,    //시간
     //유저ID
){  //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("","","","",System.currentTimeMillis())
}
