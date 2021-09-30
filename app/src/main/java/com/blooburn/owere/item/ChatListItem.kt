package com.blooburn.owere.item

//유저별 채팅방 리스트

data class ChatListItem(
    val lastMessage: String,    //마지막 메시지
    val profileImg: String, //프로필이미지
    val chatRoomId: String,    //채팅방 ID
    val opponentName : String, //상대 이름
    val opponentId : String   //상대 아이디
     //유저ID
){  //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("","","","","")
}
